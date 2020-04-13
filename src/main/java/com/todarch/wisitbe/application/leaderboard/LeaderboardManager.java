package com.todarch.wisitbe.application.leaderboard;

import com.todarch.wisitbe.domain.leaderboard.LeaderboardItem;
import com.todarch.wisitbe.domain.leaderboard.LeaderboardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardManager {
  private final ZSetOperations<String, String> sortedSetOperations;

  public LeaderboardManager(RedisTemplate<String, String> redisTemplate) {
    this.sortedSetOperations = redisTemplate.opsForZSet();
  }

  /**
   * Updates scores for given username in all available leaderboards.
   */
  public void updateScore(String username, long delta) {
    for (LeaderboardType leaderboardType : LeaderboardType.values()) {
      sortedSetOperations.incrementScore(leaderboardType.key(), username, delta);
    }
  }

  public List<LeaderboardItem> leaderboard(@NonNull LeaderboardType leaderboardType) {
    return toLeaderboard(getBoard(leaderboardType.key()));
  }

  private List<LeaderboardItem> toLeaderboard(Set<ZSetOperations.TypedTuple<String>> typedTuples) {
    var rank = 1;
    var leaderboard = new ArrayList<LeaderboardItem>();
    for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
      LeaderboardItem leaderboardItem = new LeaderboardItem();
      leaderboardItem.setRank(rank++);
      leaderboardItem.setScore(typedTuple.getScore().longValue());
      leaderboardItem.setUsername(typedTuple.getValue());
      leaderboard.add(leaderboardItem);
    }

    return leaderboard;
  }

  private Set<ZSetOperations.TypedTuple<String>> getBoard(@NonNull String boardKey) {
    // when user does not know the answer, score goes minus
    return sortedSetOperations.rangeByScoreWithScores(boardKey, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  /**
   * Cleans data in leaderboards.
   * Useful for development/debugging.
   */
  public void clean(@NonNull List<LeaderboardType> leaderboardTypes) {
    leaderboardTypes.forEach(leaderboardType -> {
      sortedSetOperations.removeRange(leaderboardType.key(), Long.MIN_VALUE, Long.MAX_VALUE);
    });
  }
}
