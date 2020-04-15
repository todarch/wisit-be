package com.todarch.wisitbe.infrastructure.messaging.listener;

import static com.todarch.wisitbe.domain.question.UserQuestionFactory.createQuestionForUser;

import com.todarch.wisitbe.application.leaderboard.LeaderboardManager;
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
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionReportedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.ReportedQuestionResolvedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.ScoreChangedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
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

  private final WisitEventPublisher wisitEventPublisher;

  private final LeaderboardManager leaderboardManager;

  /**
   * Reacts to new user creation.
   */
  @EventListener
  @Async
  public void onUserCreated(UserCreatedEvent userCreatedEvent) {
    log.info("tid={} new user created with id: {}", tid(), userCreatedEvent.getUserId());

    List<UserQuestion> questionsForUser = questionRepository.findAllActive()
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

          ScoreChangedEvent scoreChangedEvent = new ScoreChangedEvent();
          scoreChangedEvent.setUsername(getUsername(userQuestion.getUserId()));
          scoreChangedEvent.setDelta(event.isKnew() ? 10 : -5);
          wisitEventPublisher.publishEvent(scoreChangedEvent);
        });
  }

  private String getUsername(String userId) {
    // user cannot answer a question if haven't picked a username
    // at this point, we are sure that we have picked username
    return userRepository.findById(userId).get().pickedUsername().get();
  }

  /**
   * Updates score.
   */
  @EventListener
  @Async
  public void onScoreChanged(ScoreChangedEvent scoreChangedEvent) {
    leaderboardManager.updateScore(scoreChangedEvent.getUsername(), scoreChangedEvent.getDelta());
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

  /**
   * Reacts to a question reporting.
   */
  @EventListener
  @Async
  public void onQuestionReported(QuestionReportedEvent questionReportedEvent) {
    String questionId = questionReportedEvent.getQuestionId();

    Question question = questionRepository.tryToFindById(questionId);
    question.inactivate();
    questionRepository.save(question);

    // let's only disable question itself for now
    // so at least it will not be processed to create new user questions
    // if we will decide to remove from user questions, we could run into problem
    // could be timing issues such as question is asked, removed, answered
    // maybe we could just disable user questions
    // anyway we do not have to keep them there, we remove them after asking
  }

  /**
   * Reacts when a question report is resolved.
   */
  @EventListener
  @Async
  public void onReportedQuestionResolved(ReportedQuestionResolvedEvent resolvedEvent) {
    String questionId = resolvedEvent.getQuestionId();

    Question question = questionRepository.tryToFindById(questionId);

    if (!resolvedEvent.isDisable()) {
      question.activate();
      questionRepository.save(question);
    }
  }
}
