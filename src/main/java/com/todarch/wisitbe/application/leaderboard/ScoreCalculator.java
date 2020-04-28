package com.todarch.wisitbe.application.leaderboard;

import org.springframework.stereotype.Service;

@Service
public class ScoreCalculator {

  /**
   * Calculates the score to be given for the answer of a question.
   */
  public int calculate(boolean knew) {
    if (knew) {
      return 10;
    } else {
      return -5;
    }
  }
}
