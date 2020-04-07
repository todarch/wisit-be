package com.todarch.wisitbe.infrastructure.messaging.publisher;

import com.todarch.wisitbe.infrastructure.messaging.event.WisitEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WisitEventPublisher {
  private final ApplicationEventPublisher publisher;

  public void publishEvent(WisitEvent wisitEvent) {
    publisher.publishEvent(wisitEvent);
  }
}
