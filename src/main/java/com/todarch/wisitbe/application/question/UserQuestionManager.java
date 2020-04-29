package com.todarch.wisitbe.application.question;

import static java.util.Objects.requireNonNull;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionFactory;
import com.todarch.wisitbe.domain.question.UserQuestionPickingSpecification;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.AlmostAllUserQuestionsAskedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.provider.TimeProvider;
import com.todarch.wisitbe.rest.question.AnswerQuestion;
import com.todarch.wisitbe.rest.question.AnswerUserQuestion;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import com.todarch.wisitbe.rest.question.PreparedUserQuestion;
import com.todarch.wisitbe.rest.question.QuestionAnswer;
import com.todarch.wisitbe.rest.question.UserQuestionAnswer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQuestionManager {
  protected static final int NEW_QUESTION_PICKING_LIMIT = 20;

  private final QuestionRepository questionRepository;

  private final UserQuestionRepository userQuestionRepository;

  private final UserRepository userRepository;

  private final TimeProvider timeProvider;

  private final WisitEventPublisher wisitEventPublisher;

  private final LocationManager locationManager;

  private final QuestionManager questionManager;

  private final UserQuestionPickingSpecification userQuestionPickingSpecification;

  /**
   * Picks new questions for the user.
   */
  public void pickFor(String userId) {
    User user = userRepository.getById(userId);

    List<Question> notYetAskedQuestions = pickNotYetAskedOnes(user);

    List<UserQuestion> newUserQuestions = toUserQuestions(user.getId(), notYetAskedQuestions);

    userQuestionRepository.saveAll(newUserQuestions);

    LocalDateTime newPivotDate = determineNewPivotDate(notYetAskedQuestions);
    user.setPivotPoint(newPivotDate);
    userRepository.save(user);
  }

  private List<Question> pickNotYetAskedOnes(User user) {
    return questionRepository.pick20QuestionsCreatedAfter(user.pivotPoint());
  }

  private LocalDateTime determineNewPivotDate(List<Question> notYetAskedQuestions) {
    if (notYetAskedQuestions.size() >= NEW_QUESTION_PICKING_LIMIT) {
      Question lastPickedQuestion = notYetAskedQuestions.get(notYetAskedQuestions.size() - 1);
      return lastPickedQuestion.createdAt();
    } else {
      return timeProvider.now();
    }
  }

  private List<UserQuestion> toUserQuestions(String userId, List<Question> questions) {
    return questions.stream()
        .map(question -> UserQuestionFactory.createQuestionForUser(userId, question))
        .collect(Collectors.toList());
  }

  public void removeQuestion(String questionId) {
    userQuestionRepository.deleteAllByQuestionId(questionId);
  }

  /**
   * Gets next question for the user.
   * @deprecated instead of requesting single question, use nextBatchFor for multiple
   */
  @Deprecated(forRemoval = true)
  public Optional<PreparedUserQuestion> nextFor(String userId) {
    User user = userRepository.getById(userId);

    Optional<UserQuestion> optionalNextQuestion = userQuestionRepository.nextFor(userId);

    List<UserQuestion> toList = optionalNextQuestion.map(List::of).orElse(List.of());

    if (userQuestionPickingSpecification.isSatisfiedBy(user, toList)) {
      AlmostAllUserQuestionsAskedEvent event = new AlmostAllUserQuestionsAskedEvent();
      event.setUserId(userId);
      wisitEventPublisher.publishEvent(event);
    }

    if (toList.isEmpty()) {
      return Optional.empty();
    }

    UserQuestion userQuestion = toList.get(0);

    return Optional.of(toQuestionWithNoAnswer(userQuestion));
  }

  private PreparedUserQuestion toQuestionWithNoAnswer(UserQuestion userQuestion) {
    PreparedQuestion preparedQuestion =
        questionManager.toQuestionWithNoAnswer(userQuestion.getQuestion());

    PreparedUserQuestion preparedUserQuestion = new PreparedUserQuestion();
    preparedUserQuestion.setPreparedQuestion(preparedQuestion);
    preparedUserQuestion.setUserQuestionId(userQuestion.getId());
    return preparedUserQuestion;
  }

  /**
   * A user answers one of their question.
   * //TODO: handle the case if user answers a reported/removed user question
   * @param userId who is answering the question
   * @param answerUserQuestion which question is being answered
   * @return the given answer and correct answer
   */
  public UserQuestionAnswer answer(@NonNull String userId,
                                   @NonNull AnswerUserQuestion answerUserQuestion) {
    String userQuestionId = answerUserQuestion.getUserQuestionId();
    requireNonNull(userQuestionId, "UserQuestionId is required");
    AnswerQuestion answerQuestion =
        requireNonNull(answerUserQuestion.getAnswerQuestion(), "AnswerQuestion is required");

    UserQuestion userQuestion = userQuestionRepository.getByIdAndUserId(userQuestionId, userId);

    final boolean knew = userQuestion.answer(answerQuestion.getCityId());

    userQuestionRepository.save(userQuestion);

    QuestionAnswer questionAnswer =
        questionManager.toQuestionAnswer(userQuestion.getQuestion(), answerQuestion);

    UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
    userQuestionAnswer.setUserQuestionId(userQuestion.getId());
    userQuestionAnswer.setQuestionAnswer(questionAnswer);

    UserQuestionAnsweredEvent userQuestionAnsweredEvent = new UserQuestionAnsweredEvent();
    userQuestionAnsweredEvent.setKnew(knew);
    userQuestionAnsweredEvent.setUserQuestionId(userQuestionAnswer.getUserQuestionId());
    userQuestionAnsweredEvent.setAnsweredInSeconds(answerQuestion.getAnsweredInSeconds());
    userQuestionAnsweredEvent.setScoreDelta(questionAnswer.getScoreDelta());
    wisitEventPublisher.publishEvent(userQuestionAnsweredEvent);

    return userQuestionAnswer;
  }

  /**
   * Provides next batch of questions for user.
   */
  public List<PreparedUserQuestion> nextBatchFor(@NonNull String userId) {
    User user = userRepository.getById(userId);

    List<UserQuestion> nextQuestions = userQuestionRepository.next10(user.id());

    if (userQuestionPickingSpecification.isSatisfiedBy(user, nextQuestions)) {
      AlmostAllUserQuestionsAskedEvent event = new AlmostAllUserQuestionsAskedEvent();
      event.setUserId(userId);
      wisitEventPublisher.publishEvent(event);
    }

    return nextQuestions
        .stream()
        .map(this::toQuestionWithNoAnswer)
        .collect(Collectors.toList());
  }
}
