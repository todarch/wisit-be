package com.todarch.wisitbe.rest.reporting;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolveReportedQuestionCmd {
  @Size(min = 1)
  private long reportingId;
  private boolean disableQuestion;
}
