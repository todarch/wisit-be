package com.todarch.wisitbe.rest.reporting;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolveReportedQuestionCmd {
  @Min(value = 1)
  private long reportingId;
  private boolean inactivateQuestion;
}
