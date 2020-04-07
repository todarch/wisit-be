package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class PictureCreatedEvent implements WisitEvent {
  private long createdPictureId;
}
