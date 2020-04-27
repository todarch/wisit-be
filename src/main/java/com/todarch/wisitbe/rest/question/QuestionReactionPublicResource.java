package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionReactionManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question-reactions")
@AllArgsConstructor
@Slf4j
public class QuestionReactionPublicResource {

  private final QuestionReactionManager questionReactionManager;

  /**
   * User likes a question.
   */
  @GetMapping("/stats/{questionId}")
  public ResponseEntity<QuestionReactionStats> stats(@PathVariable String questionId) {
    QuestionReactionStats stats = questionReactionManager.stats(questionId);

    return ResponseEntity.ok(stats);
  }

}
