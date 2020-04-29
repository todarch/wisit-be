package com.todarch.wisitbe.application.leaderboard;

import com.todarch.wisitbe.domain.question.QuestionType;
import org.springframework.stereotype.Service;

@Service
public class ScoreCalculator {

  /**
   * Calculates the score to be given for the answer of a question.
   */
  public int calculate(boolean knew, QuestionType questionType) {
    switch (questionType) {
      case CITIES_AS_CHOICES:
        return knew ? 10 : -5;
      case COUNTRIES_AS_CHOICES:
        return knew ? 8 : -3;
      default:
        throw new AssertionError("Unknown question type");
    }
  }
}
