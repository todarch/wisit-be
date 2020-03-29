package com.todarch.wisitbe.rest.game;

import com.todarch.wisitbe.application.question.QuestionManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@AllArgsConstructor
public class QuestionResource {

  private QuestionManager questionManager;


  @GetMapping("/next")
  public ResponseEntity<SimpleQuestion> nextQuestion() {
    SimpleQuestion question = questionManager.next();
    return ResponseEntity.ok(question);
  }
}
