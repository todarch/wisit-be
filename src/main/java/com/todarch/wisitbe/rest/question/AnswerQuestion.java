package com.todarch.wisitbe.rest.question;

import lombok.Data;

@Data
public class AnswerQuestion {
  private String questionId;
  private long cityId;
  private long answeredInSeconds;
}