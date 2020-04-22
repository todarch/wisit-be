package com.todarch.wisitbe.data;

import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionFactory;
import com.todarch.wisitbe.domain.user.User;

public class UserQuestionData {

  public static UserQuestion newUserQuestionFor(User user) {
    return UserQuestionFactory.createQuestionForUser(user.id(), QuestionData.newQuestion());
  }
}
