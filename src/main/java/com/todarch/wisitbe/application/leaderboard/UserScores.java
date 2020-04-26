package com.todarch.wisitbe.application.leaderboard;

import lombok.Data;

@Data
public class UserScores {
  private long daily;
  private long weekly;
  private long monthly;
}
