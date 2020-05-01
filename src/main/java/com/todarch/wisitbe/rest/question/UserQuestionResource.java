package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.UserQuestionManager;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected/user-questions")
@AllArgsConstructor
@Slf4j
public class UserQuestionResource {
  private final CurrentUserProvider currentUserProvider;

  private final UserQuestionManager userQuestionManager;

  //TODO: possibly this cache will contain reported question
  // for now, user will be informed when answering such questions
  // aim not to ask reported questions to user in the first place
  private final Map<String, List<PreparedUserQuestion>> cachedUserQuestions =
      new ConcurrentHashMap<>();

  /**
   * Lets user answer one of their question and got feedback about the same question.
   */
  @PostMapping("/answer")
  public ResponseEntity<UserQuestionAnswer> answerQuestion(@RequestBody AnswerUserQuestion answer) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    UserQuestionAnswer userQuestionAnswer =
        userQuestionManager.answer(currentUser.id(), answer);

    removeAskedOrReportedQuestionFromCache(currentUser.username());

    return ResponseEntity.ok(userQuestionAnswer);
  }

  private void removeAskedOrReportedQuestionFromCache(String username) {
    List<PreparedUserQuestion> nextQuestions = cachedUserQuestions.get(username);
    if (!CollectionUtils.isEmpty(nextQuestions)) {
      nextQuestions.remove(0);
    }
  }

  /**
   * Returns the list of questions for the current user.
   */
  @GetMapping("/next")
  public ResponseEntity<PreparedUserQuestion> nextUserQuestion(
      @RequestParam(required = false) boolean afterReporting) {
    CurrentUser currentUser = currentUserProvider.currentUser();

    if (afterReporting) {
      removeAskedOrReportedQuestionFromCache(currentUser.username());
    }

    List<PreparedUserQuestion> nextQuestions =
        cachedUserQuestions.get(currentUser.username());

    if (CollectionUtils.isEmpty(nextQuestions)) {
      nextQuestions = userQuestionManager.nextBatchFor(currentUser.id());
    }

    cachedUserQuestions.put(currentUser.username(), nextQuestions);

    if (nextQuestions.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    PreparedUserQuestion nextQuestion = nextQuestions.get(0);
    return ResponseEntity.ok(nextQuestion);
  }

}
