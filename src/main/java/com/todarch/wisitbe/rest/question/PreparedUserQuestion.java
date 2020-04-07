package com.todarch.wisitbe.rest.question;

import java.util.Set;
import lombok.Data;

@Data
public class PreparedUserQuestion {
  private String userQuestionId;
  private String picUrl;
  private Set<String> choices;
  private Set<Long> choiceCityIds;
}
