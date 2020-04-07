package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import com.todarch.wisitbe.infrastructure.messaging.event.UserQuestionAnsweredEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
@Slf4j
public class QuestionResource {

  private final QuestionManager questionManager;

  private final CurrentUserProvider currentUserProvider;

  private final WisitEventPublisher wisitEventPublisher;

  @InternalOnly
  @PostMapping
  public ResponseEntity<Question> createQuestion(@RequestBody NewQuestionReq newQuestionReq) {
    long tid = Thread.currentThread().getId();
    log.info("tid={} ", tid);
    Question createdQuestion = questionManager.createQuestion(newQuestionReq);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
  }

  @GetMapping("/next")
  public ResponseEntity<PreparedUserQuestion> nextQuestion() {
    CurrentUser currentUser = currentUserProvider.currentUser();
    var optionalQuestion = questionManager.nextFor(currentUser.getId());
    if (optionalQuestion.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(optionalQuestion.get());
  }

  @GetMapping("/{questionId}")
  public ResponseEntity<PreparedQuestion> questionById(@PathVariable String questionId) {
    var optionalQuestion = questionManager.getById(questionId);
    if (optionalQuestion.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(optionalQuestion.get());
  }

  @PostMapping("/answer")
  public ResponseEntity<UserQuestionAnswer> answerQuestion(@RequestBody AnswerUserQuestion answerUserQuestion) {
    CurrentUser currentUser = currentUserProvider.currentUser();
    UserQuestionAnswer userQuestionAnswer = questionManager.answer(currentUser.getId(), answerUserQuestion);

    UserQuestionAnsweredEvent userQuestionAnsweredEvent = new UserQuestionAnsweredEvent();
    userQuestionAnsweredEvent.setKnew(userQuestionAnswer.isKnew());
    userQuestionAnsweredEvent.setUserQuestionId(userQuestionAnswer.getUserQuestionId());
    wisitEventPublisher.publishEvent(userQuestionAnsweredEvent);

    return ResponseEntity.ok(userQuestionAnswer);
  }


}
