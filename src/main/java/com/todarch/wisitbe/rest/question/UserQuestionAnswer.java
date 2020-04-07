package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.fix.City;
import lombok.Data;

@Data
public class UserQuestionAnswer {
  private String userQuestionId;
  private City correctCity;
  private City givenCity;
  private boolean knew;

}
