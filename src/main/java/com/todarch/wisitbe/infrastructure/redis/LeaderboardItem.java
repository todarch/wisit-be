package com.todarch.wisitbe.infrastructure.redis;

import lombok.Data;

@Data
public class LeaderboardItem {
  private int rank;
  private String username;
  private long score;
}
