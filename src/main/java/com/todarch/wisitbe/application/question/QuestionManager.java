package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.leaderboard.ScoreCalculator;
import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.domain.question.AskedQuestionRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import com.todarch.wisitbe.rest.question.AnswerQuestion;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import com.todarch.wisitbe.rest.question.QuestionAnswer;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionManager {

  private final QuestionRepository questionRepository;

  private final LocationManager locationManager;

  private final PictureRepository pictureRepository;

  private final WisitEventPublisher wisitEventPublisher;

  private final AskedQuestionRepository askedQuestionRepository;

  private final ScoreCalculator scoreCalculator;

  /**
   * Creates a question from a picture.
   */
  public void createQuestion(long pictureId) {
    Picture foundPicture =
        pictureRepository.findById(pictureId)
        .orElseThrow(() -> new ResourceNotFoundException("Picture not found: " + pictureId));

    Question savedQuestion = doCreateQuestion(foundPicture);

    QuestionCreatedEvent questionCreatedEvent = new QuestionCreatedEvent();
    questionCreatedEvent.setQuestionId(savedQuestion.getId());
    wisitEventPublisher.publishEvent(questionCreatedEvent);
  }

  private Question doCreateQuestion(Picture picture) {
    Set<Long> choices = locationManager.prepareChoices(picture.getCityId());
    Question newQuestion = new Question(UUID.randomUUID(), picture, choices);
    return questionRepository.save(newQuestion);
  }

  public Optional<PreparedQuestion> getById(String questionId) {
    return questionRepository.findById(questionId)
        .map(this::toQuestionWithNoAnswer);
  }

  PreparedQuestion toQuestionWithNoAnswer(Question question) {
    PreparedQuestion preparedQuestion = new PreparedQuestion();
    preparedQuestion.setQuestionId(question.getId());
    preparedQuestion.setPicUrl(question.pictureUrl());
    preparedQuestion.setCreatedAt(question.createdAt());
    preparedQuestion.setChoices(locationManager.toChoices(question.choices()));
    preparedQuestion.setAnsweredCount(answeredCount(question.getId()));
    return preparedQuestion;
  }

  private long answeredCount(String questionId) {
    return askedQuestionRepository.countByQuestionId(questionId);
  }

  /**
   * Gets a random question among existing ones.
   */
  public Optional<PreparedQuestion> randomQuestion() {
    long qty = questionRepository.count();
    int idx = ThreadLocalRandom.current().nextInt((int) qty);
    Page<Question> questionPage = questionRepository.findAllActive(PageRequest.of(idx, 1));
    return Optional.of(questionPage)
        .filter(Page::hasContent)
        .map(page -> page.getContent().get(0))
        .map(this::toQuestionWithNoAnswer);
  }

  public QuestionAnswer answer(@NonNull AnswerQuestion answer) {
    Question question = questionRepository.getById(answer.getQuestionId());
    return toQuestionAnswer(question, answer);
  }

  QuestionAnswer toQuestionAnswer(Question question, AnswerQuestion answerQuestion) {
    boolean knew = question.isCorrectAnswer(answerQuestion.getCityId());
    int scoreDelta = scoreCalculator.calculate(knew, answerQuestion.getQuestionType());

    QuestionAnswer questionAnswer = new QuestionAnswer();
    questionAnswer.setCorrectChoice(locationManager.toChoice(question.answerCityId()));
    questionAnswer.setGivenChoice(locationManager.toChoice(answerQuestion.getCityId()));
    questionAnswer.setKnew(knew);
    questionAnswer.setScoreDelta(scoreDelta);
    return questionAnswer;
  }
}
