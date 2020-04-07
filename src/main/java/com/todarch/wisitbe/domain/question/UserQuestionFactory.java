package com.todarch.wisitbe.domain.question;

import java.util.Objects;
import java.util.UUID;

public class UserQuestionFactory {

  public static UserQuestion createQuestionForUser(String userId, Question question) {
    Objects.requireNonNull(userId, "userId is required for user question creation");
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setId(UUID.randomUUID().toString());
    userQuestion.setUserId(userId);
    userQuestion.setQuestion(question);
    return userQuestion;
  }
}
