package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.fix.City;
import lombok.Data;

@Data
public class QuestionAnswer {
  private String questionId;
  private City correctCity;
  private City givenCity;
  private boolean knew;

}
