package com.todarch.wisitbe.domain.leaderboard;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import org.springframework.util.StringUtils;

public enum LeaderboardType {
  DAILY {
    @Override
    public String key() {
      return LeaderboardType.dailyKey();
    }
  },
  WEEKLY {
    @Override
    public String key() {
      return LeaderboardType.weeklyKey();
    }
  },
  MONTHLY {
    @Override
    public String key() {
      return LeaderboardType.monthlyKey();
    }
  };

  public static List<LeaderboardType> from(String type) {
    if (!StringUtils.hasText(type)) {
      return List.of();
    }
    String capital = type.substring(0, 1).toUpperCase();
    if (capital.equals("A")) {
      return List.of(LeaderboardType.values());
    }

    for (LeaderboardType leaderboardType : LeaderboardType.values()) {
      if (leaderboardType.name().startsWith(capital)) {
        return List.of(leaderboardType);
      }
    }

    return List.of();
  }

  public abstract String key();


  /**
   * Provides key for daily leaderboard.
   * example: 2020-04-12
   */
  private static String dailyKey() {
    return "TD_LEADERBOARD_DAILY_" + LocalDate.now().toString();
  }

  /**
   * Provides key for weekly leaderboard.
   */
  private static String weeklyKey() {
    LocalDate date = LocalDate.now();
    int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    return "TD_LEADERBOARD_WEEKLY_" + weekNumber;
  }

  /**
   * Provides key for monthly leaderboard.
   */
  private static String monthlyKey() {
    LocalDate date = LocalDate.now();
    return "TD_LEADERBOARD_MONTHLY_" + date.getMonth().name();
  }
}
