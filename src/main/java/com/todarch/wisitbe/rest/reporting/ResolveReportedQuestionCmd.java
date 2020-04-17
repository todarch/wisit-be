package com.todarch.wisitbe.rest.reporting;

import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class ResolveReportedQuestionCmd {
  @Min(value = 1)
  private long reportingId;
  private boolean inactivateQuestion;
}
