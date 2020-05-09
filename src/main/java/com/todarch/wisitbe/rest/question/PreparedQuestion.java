package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.Choice;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PreparedQuestion {
  private String questionId;
  private long picId;
  private String picUrl;
  private Set<Choice> choices;
  private LocalDateTime createdAt;
  private long answeredCount;
}
