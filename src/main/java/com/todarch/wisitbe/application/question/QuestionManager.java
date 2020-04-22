package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionManager {

  private final QuestionRepository questionRepository;

  private final LocationManager locationManager;

  private final PictureRepository pictureRepository;

  private final WisitEventPublisher wisitEventPublisher;

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

  private PreparedQuestion toQuestionWithNoAnswer(Question question) {
    PreparedQuestion preparedQuestion = new PreparedQuestion();
    preparedQuestion.setQuestionId(question.getId());
    preparedQuestion.setPicUrl(question.pictureUrl());
    preparedQuestion.setCreatedAt(question.createdAt());
    preparedQuestion.setChoices(locationManager.toCityNames(question.choices()));
    preparedQuestion.setChoiceCityIds(question.choices());
    return preparedQuestion;
  }
}
