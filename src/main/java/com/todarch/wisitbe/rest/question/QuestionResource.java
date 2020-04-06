package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
public class QuestionResource {

  private final QuestionManager questionManager;

  @InternalOnly
  @PostMapping
  public ResponseEntity<Question> createQuestion(@RequestBody NewQuestionReq newQuestionReq) {
    Question createdQuestion = questionManager.createQuestion(newQuestionReq);
    return ResponseEntity.ok(createdQuestion);
  }

  @GetMapping("/next")
  public ResponseEntity<QuestionWithNoAnswer> nextQuestion() {
    QuestionWithNoAnswer question = questionManager.nextFor("not-used-value");
    return ResponseEntity.ok(question);
  }

  @PostMapping("/answer")
  public ResponseEntity<QuestionAnswer> answerQuestion(@RequestBody AnswerQuestion answerQuestion) {
    QuestionAnswer questionAnswer = questionManager.answer(answerQuestion);
    return ResponseEntity.ok(questionAnswer);
  }


}
