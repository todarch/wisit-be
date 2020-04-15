package com.todarch.wisitbe.rest.reporting;

import com.todarch.wisitbe.application.reporting.ReportingManager;
import com.todarch.wisitbe.domain.reporting.ReportingReason;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportings")
@AllArgsConstructor
public class ReportingResource {

  private final ReportingManager reportingManager;

  @GetMapping("/reasons")
  public ResponseEntity<List<ReportingReason>> reportingReasons() {
    return ResponseEntity.ok(reportingManager.reportingReasons());
  }

  @PostMapping
  public ResponseEntity<Void> reportQuestion(
      @Valid @RequestBody ReportQuestionCmd reportQuestionCmd) {
    reportingManager.accept(reportQuestionCmd);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/resolve")
  public ResponseEntity<Void> resolveReportedQuestion(
      @Valid @RequestBody ResolveReportedQuestionCmd resolveReportedQuestionCmd) {
    reportingManager.resolve(resolveReportedQuestionCmd);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
