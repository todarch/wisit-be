package com.todarch.wisitbe.rest.reporting;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportQuestionCmd {
  @NotBlank
  private String questionId;
  private int reportingReasonId;
  @Size(max = 250)
  private String detail;
}
