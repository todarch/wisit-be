package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * Returns the a random question for the guest user.
   */
  @GetMapping("/random")
  public ResponseEntity<PreparedQuestion> nextQuestion() {
    var optionalQuestion = questionManager.randomQuestion();

    if (optionalQuestion.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(optionalQuestion.get());
  }

  /**
   * Returns the details of a question by id.
   */
  @GetMapping("/{questionId}")
  public ResponseEntity<PreparedQuestion> questionById(@PathVariable String questionId) {
    var optionalQuestion = questionManager.getById(questionId);
    if (optionalQuestion.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(optionalQuestion.get());
  }

  /**
   * Answers a question.
   */
  @PostMapping("/answer")
  public ResponseEntity<QuestionAnswer> answerQuestion(@RequestBody AnswerQuestion answer) {

    QuestionAnswer questionAnswer = questionManager.answer(answer);

    return ResponseEntity.ok(questionAnswer);
  }

}
