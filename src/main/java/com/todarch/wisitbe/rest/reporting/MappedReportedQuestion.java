package com.todarch.wisitbe.rest.reporting;

import com.todarch.wisitbe.rest.question.PreparedQuestion;
import lombok.Data;

@Data
public class MappedReportedQuestion {
  private long id;
  private PreparedQuestion question;
  private String reason;
  private String detail;
}
