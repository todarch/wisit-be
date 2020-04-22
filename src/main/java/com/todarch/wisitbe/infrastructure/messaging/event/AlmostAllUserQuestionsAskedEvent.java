package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class AlmostAllUserQuestionsAskedEvent implements WisitEvent {
  private String userId;
}
