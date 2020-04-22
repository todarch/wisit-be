package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionFactory;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.AlmostAllUserQuestionsAskedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.provider.TimeProvider;
import com.todarch.wisitbe.rest.question.AnswerUserQuestion;
import com.todarch.wisitbe.rest.question.PreparedUserQuestion;
import com.todarch.wisitbe.rest.question.UserQuestionAnswer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
   */
  public Optional<PreparedUserQuestion> nextFor(String userId) {
    User user = userRepository.getById(userId);

    Optional<UserQuestion> optionalNextQuestion = userQuestionRepository.nextFor(userId);

    if (optionalNextQuestion.isEmpty()) {
      AlmostAllUserQuestionsAskedEvent event = new AlmostAllUserQuestionsAskedEvent();
      event.setUserId(userId);
      wisitEventPublisher.publishEvent(event);
      return Optional.empty();
    }

    UserQuestion userQuestion = optionalNextQuestion.get();

    if (!userQuestion.isNew() && user.isEligibleForMoreQuestions()) {
      AlmostAllUserQuestionsAskedEvent event = new AlmostAllUserQuestionsAskedEvent();
      event.setUserId(userId);
      wisitEventPublisher.publishEvent(event);
    }

    return Optional.of(toQuestionWithNoAnswer(userQuestion));
  }

  private PreparedUserQuestion toQuestionWithNoAnswer(UserQuestion userQuestion) {
    Question question = userQuestion.getQuestion();
    PreparedUserQuestion preparedUserQuestion = new PreparedUserQuestion();
    preparedUserQuestion.setQuestionId(question.getId());
    preparedUserQuestion.setUserQuestionId(userQuestion.getId());
    preparedUserQuestion.setPicUrl(question.pictureUrl());
    preparedUserQuestion.setCreatedAt(question.createdAt());
    preparedUserQuestion.setChoices(locationManager.toCities(question.choices()));
    preparedUserQuestion.setAnsweredCount(questionManager.answeredCount(question.getId()));
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
    Objects.requireNonNull(userQuestionId, "UserQuestionId is required");

    UserQuestion userQuestion = userQuestionRepository.getByIdAndUserId(userQuestionId, userId);

    final boolean knew = userQuestion.answer(answerUserQuestion.getCityId());

    userQuestionRepository.save(userQuestion);

    UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
    userQuestionAnswer.setUserQuestionId(userQuestion.getId());
    userQuestionAnswer.setCorrectCity(locationManager.getCityById(userQuestion.correctAnswer()));
    userQuestionAnswer.setGivenCity(locationManager.getCityById(answerUserQuestion.getCityId()));
    userQuestionAnswer.setKnew(knew);

    UserQuestionAnsweredEvent userQuestionAnsweredEvent = new UserQuestionAnsweredEvent();
    userQuestionAnsweredEvent.setKnew(userQuestionAnswer.isKnew());
    userQuestionAnsweredEvent.setUserQuestionId(userQuestionAnswer.getUserQuestionId());
    userQuestionAnsweredEvent.setAnsweredInSeconds(answerUserQuestion.getAnsweredInSeconds());
    wisitEventPublisher.publishEvent(userQuestionAnsweredEvent);

    return userQuestionAnswer;
  }
}
