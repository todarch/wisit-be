package com.todarch.wisitbe.rest.question;

import lombok.Data;

@Data
public class AnswerUserQuestion {
  private String userQuestionId;
  private AnswerQuestion answerQuestion;
}
