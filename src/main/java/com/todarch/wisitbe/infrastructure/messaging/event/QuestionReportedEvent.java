package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class QuestionReportedEvent implements WisitEvent {
  private String questionId;
}
