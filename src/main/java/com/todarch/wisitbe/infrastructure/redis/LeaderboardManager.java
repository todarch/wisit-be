package com.todarch.wisitbe.infrastructure.redis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
   * Provides key for daily leaderboard.
   * example: 2020-04-12
   */
  protected String dailyKey() {
    return LocalDate.now().toString();
  }

  public void updateScore(String username, long diff) {
    sortedSetOperations.incrementScore(dailyKey(), username, diff);
  }

  /**
   * Gives a state of the daily leader board.
   */
  public List<LeaderboardItem> dailyLeaderboard() {
    // when user does not know the answer, score goes minus
    Set<ZSetOperations.TypedTuple<String>> typedTuples =
        sortedSetOperations.rangeByScoreWithScores(dailyKey(), Long.MIN_VALUE, Long.MAX_VALUE);

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

  public void cleanDailyLeaderboard() {
    sortedSetOperations.removeRange(dailyKey(), Long.MIN_VALUE, Long.MAX_VALUE);
  }
}
