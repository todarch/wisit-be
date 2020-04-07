package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class UserQuestionAnsweredEvent implements WisitEvent {
  private String userQuestionId;
  private boolean knew;
}