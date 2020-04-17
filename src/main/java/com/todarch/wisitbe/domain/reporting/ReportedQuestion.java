package com.todarch.wisitbe.domain.reporting;

import com.todarch.wisitbe.domain.picture.Picture;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

  @OneToOne
  @JoinColumn(name = "reporting_reason_id", referencedColumnName = "id")
  private ReportingReason reportingReason;

  private String detail;

  private boolean resolved;

  public void resolve() {
    this.resolved = true;
  }
}
