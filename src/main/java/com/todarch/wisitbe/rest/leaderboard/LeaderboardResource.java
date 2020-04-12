package com.todarch.wisitbe.rest.leaderboard;

import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import com.todarch.wisitbe.infrastructure.redis.LeaderboardItem;
import com.todarch.wisitbe.infrastructure.redis.LeaderboardManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboards")
@AllArgsConstructor
public class LeaderboardResource {

  private final LeaderboardManager leaderboardManager;


  @GetMapping("/daily")
  public ResponseEntity<List<LeaderboardItem>> dailyLeaderboard() {
    List<LeaderboardItem> dailyLeaderboard = leaderboardManager.dailyLeaderboard();
    return ResponseEntity.ok(dailyLeaderboard);
  }

  @InternalOnly
  @PostMapping("/daily/clean")
  public void cleanDailyLeaderboard() {
    leaderboardManager.cleanDailyLeaderboard();
  }
}
