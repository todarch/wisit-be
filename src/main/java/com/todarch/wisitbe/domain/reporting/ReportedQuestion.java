package com.todarch.wisitbe.domain.reporting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "reported_questions")
@Entity
@Data
public class ReportedQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String questionId;

  private int reportingReasonId;

  private String detail;

  private boolean resolved;

  public void resolve() {
    this.resolved = true;
  }
}
