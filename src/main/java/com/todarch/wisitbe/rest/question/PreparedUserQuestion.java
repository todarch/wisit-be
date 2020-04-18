package com.todarch.wisitbe.rest.question;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime createdAt;
}
