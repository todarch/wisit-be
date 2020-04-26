package com.todarch.wisitbe.rest.leaderboard;

import com.todarch.wisitbe.application.leaderboard.LeaderboardManager;
import com.todarch.wisitbe.application.leaderboard.UserScores;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected/scores")
@AllArgsConstructor
public class ScoreResource {

  private final LeaderboardManager leaderboardManager;

  private final CurrentUserProvider currentUserProvider;

  /**
   * Provides access to current user's score.
   */
  @GetMapping
  public ResponseEntity<UserScores> userScores() {
    String currentUsername = currentUserProvider.currentUser().username();
    UserScores userScores = leaderboardManager.userScores(currentUsername);
    return ResponseEntity.ok(userScores);
  }
}
