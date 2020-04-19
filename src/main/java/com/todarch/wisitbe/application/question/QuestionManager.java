package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.InvalidInputException;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import com.todarch.wisitbe.rest.question.AnswerUserQuestion;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import com.todarch.wisitbe.rest.question.PreparedUserQuestion;
import com.todarch.wisitbe.rest.question.UserQuestionAnswer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionManager {

  private final QuestionRepository questionRepository;

  private final LocationManager locationManager;

  private final PictureRepository pictureRepository;

  private final WisitEventPublisher wisitEventPublisher;

  private final UserQuestionRepository userQuestionRepository;

  /**
   * Creates a question from a picture.
   */
  public void createQuestion(long pictureId) {
    Picture foundPicture =
        pictureRepository.findById(pictureId)
        .orElseThrow(() -> new ResourceNotFoundException("Picture not found: " + pictureId));

    doCreateQuestion(foundPicture);
  }

  private Question doCreateQuestion(Picture picture) {
    Set<Long> choices = locationManager.prepareChoices(picture.getCityId());
    Question newQuestion = new Question(UUID.randomUUID(), picture, choices);
    Question savedQuestion = questionRepository.save(newQuestion);

    QuestionCreatedEvent questionCreatedEvent = new QuestionCreatedEvent();
    questionCreatedEvent.setQuestionId(savedQuestion.getId());
    wisitEventPublisher.publishEvent(questionCreatedEvent);

    return savedQuestion;
  }

  /**
   * Finds the next question for the user if exists.
   */
  public Optional<PreparedUserQuestion> nextFor(String userId) {
    List<UserQuestion> allQuestionsForUser = userQuestionRepository.findAllByUserId(userId);

    // some of the prepared user questions might be already reported ones
    // get rid off them here until it becomes a headache
    while (!allQuestionsForUser.isEmpty()) {
      int randomIndex = randomIndex(allQuestionsForUser.size());
      UserQuestion userQuestion = allQuestionsForUser.get(randomIndex);
      if (userQuestion.getQuestion().isNotActive()) {
        allQuestionsForUser.remove(randomIndex);
        userQuestionRepository.delete(userQuestion);
      } else {
        return Optional.of(toQuestionWithNoAnswer(userQuestion));
      }
    }

    return Optional.empty();
  }

  private int randomIndex(int bound) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return random.nextInt(bound);
  }

  /**
   * A user answers one of their question.
   * @param userId who is answering the question
   * @param answerUserQuestion which question is being answered
   * @return the given answer and correct answer
   */
  public UserQuestionAnswer answer(@NonNull String userId,
                                   @NonNull AnswerUserQuestion answerUserQuestion) {
    String userQuestionId = answerUserQuestion.getUserQuestionId();
    Objects.requireNonNull(userQuestionId, "UserQuestionId is required");

    UserQuestion userQuestion = userQuestionRepository.findById(userQuestionId)
        .orElseThrow(() ->
            new ResourceNotFoundException("UserQuestion not found: " + userQuestionId));

    if (!userQuestion.getUserId().equals(userId)) {
      throw new InvalidInputException(userQuestionId + " user question is not for user: " + userId);
    }

    Question question = userQuestion.getQuestion();

    boolean knew = question.isCorrectAnswer(answerUserQuestion.getCityId());

    UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
    userQuestionAnswer.setUserQuestionId(userQuestion.getId());
    userQuestionAnswer.setCorrectCity(locationManager.getCityById(question.answerCityId()));
    userQuestionAnswer.setGivenCity(locationManager.getCityById(answerUserQuestion.getCityId()));
    userQuestionAnswer.setKnew(knew);

    UserQuestionAnsweredEvent userQuestionAnsweredEvent = new UserQuestionAnsweredEvent();
    userQuestionAnsweredEvent.setKnew(userQuestionAnswer.isKnew());
    userQuestionAnsweredEvent.setUserQuestionId(userQuestionAnswer.getUserQuestionId());
    userQuestionAnsweredEvent.setAnsweredInSeconds(answerUserQuestion.getAnsweredInSeconds());
    wisitEventPublisher.publishEvent(userQuestionAnsweredEvent);

    return userQuestionAnswer;
  }

  public Optional<PreparedQuestion> getById(String questionId) {
    return questionRepository.findById(questionId)
        .map(this::toQuestionWithNoAnswer);
  }

  private PreparedQuestion toQuestionWithNoAnswer(Question question) {
    PreparedQuestion preparedQuestion = new PreparedQuestion();
    preparedQuestion.setQuestionId(question.getId());
    preparedQuestion.setPicUrl(question.pictureUrl());
    preparedQuestion.setCreatedAt(question.createdAt());
    preparedQuestion.setChoices(locationManager.toCityNames(question.choices()));
    preparedQuestion.setChoiceCityIds(question.choices());
    return preparedQuestion;
  }

  private PreparedUserQuestion toQuestionWithNoAnswer(UserQuestion userQuestion) {
    Question question = userQuestion.getQuestion();
    PreparedUserQuestion preparedUserQuestion = new PreparedUserQuestion();
    preparedUserQuestion.setQuestionId(question.getId());
    preparedUserQuestion.setUserQuestionId(userQuestion.getId());
    preparedUserQuestion.setPicUrl(question.pictureUrl());
    preparedUserQuestion.setCreatedAt(question.createdAt());
    preparedUserQuestion.setChoices(locationManager.toCities(question.choices()));
    return preparedUserQuestion;
  }
}
