package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.location.City;
import lombok.Data;

@Data
public class QuestionAnswer {
  private City correctCity;
  private City givenCity;
  private boolean knew;
}
