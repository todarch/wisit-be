package com.todarch.wisitbe.rest.leaderboard;

import com.todarch.wisitbe.application.leaderboard.LeaderboardManager;
import com.todarch.wisitbe.domain.leaderboard.LeaderboardItem;
import com.todarch.wisitbe.domain.leaderboard.LeaderboardType;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboards")
@AllArgsConstructor
public class LeaderboardResource {

  private final LeaderboardManager leaderboardManager;

  /**
   * Provides access to one of the leaderboards.
   * ?type=daily|monthly|weekly
   */
  @GetMapping
  public ResponseEntity<List<LeaderboardItem>> leaderboard(@RequestParam String type) {
    List<LeaderboardType> leaderboardTypes = LeaderboardType.from(type);
    if (leaderboardTypes.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    List<LeaderboardItem> leaderboard = leaderboardManager.leaderboard(leaderboardTypes.get(0));
    return ResponseEntity.ok(leaderboard);
  }

  /**
   * Provides endpoint for cleaning data in leaderboards.
   * ?type=daily|monthly|weekly
   * Returns the list of cleaned leaderboards.
   */
  @InternalOnly
  @PostMapping("/clean")
  public ResponseEntity<List<String>> cleanLeaderboard(@RequestParam String type) {
    List<LeaderboardType> leaderboardTypes = LeaderboardType.from(type);
    leaderboardManager.clean(leaderboardTypes);
    List<String> cleanedUpList =
        leaderboardTypes.stream().map(Enum::name).collect(Collectors.toList());
    return ResponseEntity.ok(cleanedUpList);
  }
}
