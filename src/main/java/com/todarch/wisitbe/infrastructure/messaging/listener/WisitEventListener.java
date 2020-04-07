package com.todarch.wisitbe.infrastructure.messaging.listener;

import static com.todarch.wisitbe.domain.question.UserQuestionFactory.createQuestionForUser;

import com.todarch.wisitbe.domain.question.AskedQuestion;
import com.todarch.wisitbe.domain.question.AskedQuestionFactory;
import com.todarch.wisitbe.domain.question.AskedQuestionRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
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

  @EventListener
  @Async
  public void onQuestionCreated(QuestionCreatedEvent questionCreatedEvent) {
    log.info("tid={}, new question created with id: {}", tid(), questionCreatedEvent.getQuestionId());

    Optional<Question> optionalQuestion = questionRepository.findById(questionCreatedEvent.getQuestionId());

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

  @EventListener
  @Async
  public void onUserQuestionAnswered(UserQuestionAnsweredEvent userQuestionAnsweredEvent) {
    String userQuestionId = userQuestionAnsweredEvent.getUserQuestionId();

    userQuestionRepository.findById(userQuestionId)
        .ifPresent(userQuestion -> {
          AskedQuestion askedQuestion = AskedQuestionFactory.create(userQuestion, userQuestionAnsweredEvent.isKnew());
          askedQuestionRepository.save(askedQuestion);
          userQuestionRepository.deleteById(userQuestionId);
        });
  }

  private long tid() {
    return Thread.currentThread().getId();
  }
}
