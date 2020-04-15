package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.location.City;
import java.util.Set;
import lombok.Data;

@Data
public class PreparedUserQuestion {
  private String userQuestionId;
  private String questionId;
  private String picUrl;
  private Set<City> choices;
}
