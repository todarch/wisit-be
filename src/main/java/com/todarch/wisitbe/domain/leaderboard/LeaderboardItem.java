package com.todarch.wisitbe.domain.leaderboard;

import lombok.Data;

@Data
public class LeaderboardItem {
  private int rank;
  private String username;
  private long score;
}
