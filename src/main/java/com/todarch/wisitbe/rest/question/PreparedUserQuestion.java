package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.domain.location.City;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PreparedUserQuestion {
  private String userQuestionId;
  private String questionId;
  private String picUrl;
  private Set<City> choices;
  private LocalDateTime createdAt;
  private long answeredCount;
}
