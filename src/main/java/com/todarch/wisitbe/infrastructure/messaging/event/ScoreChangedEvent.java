package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class ScoreChangedEvent implements WisitEvent {
  private String username;
  private int delta;
}
