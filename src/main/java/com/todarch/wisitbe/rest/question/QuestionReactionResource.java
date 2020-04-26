package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionReactionManager;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected/question-reactions")
@AllArgsConstructor
@Slf4j
public class QuestionReactionResource {

  private final CurrentUserProvider currentUserProvider;

  private final QuestionReactionManager questionReactionManager;

  /**
   * User likes a question.
   */
  @GetMapping("/stats/{questionId}")
  public ResponseEntity<QuestionReactionStats> stats(@PathVariable String questionId) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    QuestionReactionStats stats = questionReactionManager.stats(currentUser.id(), questionId);

    return ResponseEntity.ok(stats);
  }

  /**
   * User likes a question.
   */
  @PostMapping("/like/{questionId}")
  public ResponseEntity<Void> like(@PathVariable String questionId) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    questionReactionManager.like(currentUser.id(), questionId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * User unlikes a question.
   */
  @PostMapping("/unlike/{questionId}")
  public ResponseEntity<Void> unlike(@PathVariable String questionId) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    questionReactionManager.unlike(currentUser.id(), questionId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * User dislike a question.
   */
  @PostMapping("/dislike/{questionId}")
  public ResponseEntity<Void> dislike(@PathVariable String questionId) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    questionReactionManager.dislike(currentUser.id(), questionId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * User removes dislike question.
   */
  @PostMapping("/undislike/{questionId}")
  public ResponseEntity<Void> undislike(@PathVariable String questionId) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    questionReactionManager.undislike(currentUser.id(), questionId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
