package com.todarch.wisitbe.infrastructure.messaging.listener;

import static com.todarch.wisitbe.domain.question.UserQuestionFactory.createQuestionForUser;

import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.domain.question.AskedQuestion;
import com.todarch.wisitbe.domain.question.AskedQuestionFactory;
import com.todarch.wisitbe.domain.question.AskedQuestionRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.PictureCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class WisitEventListener {

  private final QuestionRepository questionRepository;

  private final UserQuestionRepository userQuestionRepository;

  private final UserRepository userRepository;

  private final AskedQuestionRepository askedQuestionRepository;

  private final QuestionManager questionManager;

  /**
   * Reacts to new user creation.
   */
  @EventListener
  @Async
  public void onUserCreated(UserCreatedEvent userCreatedEvent) {
    log.info("tid={} new user created with id: {}", tid(), userCreatedEvent.getUserId());

    List<UserQuestion> questionsForUser = questionRepository.findAll()
        .stream()
        .map(question -> createQuestionForUser(userCreatedEvent.getUserId(), question))
        .collect(Collectors.toList());

    userQuestionRepository.saveAll(questionsForUser);
  }

  /**
   * Reacts to new question creation.
   */
  @EventListener
  @Async
  public void onQuestionCreated(QuestionCreatedEvent event) {
    log.info("tid={}, new question created with id: {}", tid(), event.getQuestionId());

    Optional<Question> optionalQuestion = questionRepository.findById(event.getQuestionId());

    if (optionalQuestion.isEmpty()) {
      return;
    }

    Question question = optionalQuestion.get();

    List<UserQuestion> questionForUsers = userRepository.findAll()
        .stream()
        .map(user -> createQuestionForUser(user.getId(), question))
        .collect(Collectors.toList());

    userQuestionRepository.saveAll(questionForUsers);
  }

  /**
   * Reacts when a user answers a question.
   */
  @EventListener
  @Async
  public void onUserQuestionAnswered(UserQuestionAnsweredEvent event) {
    String userQuestionId = event.getUserQuestionId();

    userQuestionRepository.findById(userQuestionId)
        .ifPresent(userQuestion -> {
          AskedQuestion askedQuestion = AskedQuestionFactory.create(userQuestion, event.isKnew());
          askedQuestionRepository.save(askedQuestion);
          userQuestionRepository.deleteById(userQuestionId);
        });
  }

  /**
   * Reacts to a picture creation.
   */
  @EventListener
  @Async
  public void onPictureCreated(PictureCreatedEvent pictureCreatedEvent) {
    long createdPicId = pictureCreatedEvent.getCreatedPictureId();

    questionManager.createQuestion(createdPicId);
  }

  private long tid() {
    return Thread.currentThread().getId();
  }
}
