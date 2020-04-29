package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.question.QuestionType;
import lombok.Data;

@Data
public class AnswerQuestion {
  private String questionId;
  private long cityId;
  private long answeredInSeconds;
  private QuestionType questionType;
}
