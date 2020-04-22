package com.todarch.wisitbe.domain.question;

import java.util.Objects;
import java.util.UUID;

public final class UserQuestionFactory {

  private UserQuestionFactory() {
    throw new AssertionError("No instance of utility class");
  }

  /**
   * Creates a question for a user.
   */
  public static UserQuestion createQuestionForUser(String userId, Question question) {
    Objects.requireNonNull(userId, "userId is required for user question creation");
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setId(UUID.randomUUID().toString());
    userQuestion.setUserId(userId);
    userQuestion.setQuestion(question);
    userQuestion.setWeight(UserQuestion.DEFAULT_WEIGHT);
    return userQuestion;
  }
}
