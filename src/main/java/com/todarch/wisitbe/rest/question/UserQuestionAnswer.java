package com.todarch.wisitbe.rest.question;

import lombok.Data;

@Data
public class UserQuestionAnswer {
  private String userQuestionId;
  private QuestionAnswer questionAnswer;
}
