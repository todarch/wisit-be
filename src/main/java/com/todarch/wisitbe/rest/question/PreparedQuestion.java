package com.todarch.wisitbe.rest.question;

import java.util.Set;
import lombok.Data;

@Data
public class PreparedQuestion {
  private String questionId;
  private String picUrl;
  private Set<String> choices;
  private Set<Long> choiceCityIds;
}
