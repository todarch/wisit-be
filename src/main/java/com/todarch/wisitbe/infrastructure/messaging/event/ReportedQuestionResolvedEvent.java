package com.todarch.wisitbe.infrastructure.messaging.event;

import lombok.Data;

@Data
public class ReportedQuestionResolvedEvent implements WisitEvent {
  private String questionId;
  private boolean inactivate;
}
