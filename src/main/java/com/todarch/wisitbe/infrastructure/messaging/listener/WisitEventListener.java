package com.todarch.wisitbe.infrastructure.messaging.listener;

import com.todarch.wisitbe.application.leaderboard.LeaderboardManager;
import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.application.question.UserQuestionManager;
import com.todarch.wisitbe.domain.question.AskedQuestion;
import com.todarch.wisitbe.domain.question.AskedQuestionFactory;
import com.todarch.wisitbe.domain.question.AskedQuestionRepository;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.AlmostAllUserQuestionsAskedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.PictureCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionReportedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.ReportedQuestionResolvedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.ScoreChangedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
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

  private final UserQuestionManager userQuestionManager;

  /**
   * Reacts to new user creation.
   */
  @EventListener
  @Async
  public void onUserCreated(UserCreatedEvent userCreatedEvent) {
    userQuestionManager.pickFor(userCreatedEvent.getUserId());
  }

  @EventListener
  @Async
  public void almostAllUserQuestionsAsked(AlmostAllUserQuestionsAskedEvent event) {
    userQuestionManager.pickFor(event.getUserId());
  }

  /**
   * Reacts to new question creation.
   */
  @EventListener
  @Async
  public void onQuestionCreated(QuestionCreatedEvent event) {
    // we will not add this question as a user question for users
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
          AskedQuestion askedQuestion = AskedQuestionFactory.create(
              userQuestion,
              event.isKnew(),
              event.getAnsweredInSeconds());
          askedQuestionRepository.save(askedQuestion);

          if (!userQuestion.canBeAskedAgain()) {
            userQuestionRepository.deleteById(userQuestionId);
          }

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
    userQuestionManager.removeQuestion(questionId);
  }

  /**
   * Reacts when a question report is resolved.
   */
  @EventListener
  @Async
  public void onReportedQuestionResolved(ReportedQuestionResolvedEvent resolvedEvent) {
    String questionId = resolvedEvent.getQuestionId();

    Question question = questionRepository.tryToFindById(questionId);

    if (resolvedEvent.isInactivate()) {
      question.inactivate();
    } else {
      question.activate();
    }

    questionRepository.save(question);
  }
}
