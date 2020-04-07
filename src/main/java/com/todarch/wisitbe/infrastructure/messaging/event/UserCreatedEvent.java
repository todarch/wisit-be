package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class UserCreatedEvent implements WisitEvent {
  private String userId;
}
