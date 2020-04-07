package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class QuestionCreatedEvent implements WisitEvent {
  private String questionId;
}
