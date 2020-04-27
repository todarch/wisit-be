package com.todarch.wisitbe.rest.question;

import lombok.Data;

@Data
public class PreparedUserQuestion {
  private String userQuestionId;
  private PreparedQuestion preparedQuestion;
}
