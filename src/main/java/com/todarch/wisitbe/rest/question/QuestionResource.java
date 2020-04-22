package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.application.question.UserQuestionManager;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
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

  private final CurrentUserProvider currentUserProvider;

  private final UserQuestionManager userQuestionManager;

  /**
   * Returns the next question for the current user.
   */
  @GetMapping("/next")
  public ResponseEntity<PreparedUserQuestion> nextQuestion() {
    CurrentUser currentUser = currentUserProvider.currentUser();
    currentUser.requirePickedUsername();

    var optionalQuestion = userQuestionManager.nextFor(currentUser.id());
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
   * Lets user answer one of their question and got feedback about the same question.
   */
  @PostMapping("/answer")
  public ResponseEntity<UserQuestionAnswer> answerQuestion(@RequestBody AnswerUserQuestion answer) {
    CurrentUser currentUser = currentUserProvider.currentUser();
    currentUser.requirePickedUsername();

    UserQuestionAnswer userQuestionAnswer =
        userQuestionManager.answer(currentUser.id(), answer);

    return ResponseEntity.ok(userQuestionAnswer);
  }

}
