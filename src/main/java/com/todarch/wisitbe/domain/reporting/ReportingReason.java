package com.todarch.wisitbe.domain.reporting;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "reporting_reasons")
@Entity
@Data
public class ReportingReason {

  @Id
  private int id;

  private String reason;

}
