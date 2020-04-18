package com.todarch.wisitbe.rest.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PreparedQuestion {
  private String questionId;
  private String picUrl;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime createdAt;
  private Set<String> choices;
  private Set<Long> choiceCityIds;
}
