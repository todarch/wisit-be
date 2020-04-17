package com.todarch.wisitbe.application.reporting;

import com.todarch.wisitbe.application.question.QuestionManager;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.reporting.ReportedQuestion;
import com.todarch.wisitbe.domain.reporting.ReportedQuestionRepository;
import com.todarch.wisitbe.domain.reporting.ReportingReason;
import com.todarch.wisitbe.domain.reporting.ReportingReasonRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionReportedEvent;
import com.todarch.wisitbe.infrastructure.messaging.event.ReportedQuestionResolvedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import com.todarch.wisitbe.rest.reporting.MappedReportedQuestion;
import com.todarch.wisitbe.rest.reporting.ReportQuestionCmd;
import com.todarch.wisitbe.rest.reporting.ResolveReportedQuestionCmd;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportingManager {

  private final ReportingReasonRepository reportingReasonRepository;

  private final QuestionRepository questionRepository;

  private final ReportedQuestionRepository reportedQuestionRepository;

  private final QuestionManager questionManager;

  private final WisitEventPublisher wisitEventPublisher;

  public List<ReportingReason> reportingReasons() {
    return reportingReasonRepository.findAll();
  }

  /**
   * Accepts reports for questions.
   */
  public void accept(@NonNull ReportQuestionCmd reportQuestionCmd) {
    questionRepository.findById(reportQuestionCmd.getQuestionId())
        .orElseThrow(() -> new ResourceNotFoundException("Question id not found to be reported"));

    ReportingReason reportingReason =
        reportingReasonRepository.findById(reportQuestionCmd.getReportingReasonId())
            .orElseThrow(() -> new ResourceNotFoundException("Reporting reason id not found"));

    ReportedQuestion reportedQuestion = new ReportedQuestion();
    reportedQuestion.setQuestionId(reportQuestionCmd.getQuestionId());
    reportedQuestion.setReportingReason(reportingReason);
    reportedQuestion.setDetail(reportQuestionCmd.getDetail());

    reportedQuestionRepository.save(reportedQuestion);

    QuestionReportedEvent questionReportedEvent = new QuestionReportedEvent();
    questionReportedEvent.setQuestionId(reportQuestionCmd.getQuestionId());
    wisitEventPublisher.publishEvent(questionReportedEvent);
  }

  /**
   * Resolves existing question reports.
   */
  public void resolve(@NonNull ResolveReportedQuestionCmd resolveReportedQuestionCmd) {
    ReportedQuestion reportedQuestion =
        reportedQuestionRepository.findById(resolveReportedQuestionCmd.getReportingId())
            .orElseThrow(() -> new ResourceNotFoundException("Reporting not found"));

    reportedQuestion.resolve();

    reportedQuestionRepository.save(reportedQuestion);

    ReportedQuestionResolvedEvent resolvedEvent = new ReportedQuestionResolvedEvent();
    resolvedEvent.setQuestionId(reportedQuestion.getQuestionId());
    resolvedEvent.setInactivate(resolveReportedQuestionCmd.isInactivateQuestion());
    wisitEventPublisher.publishEvent(resolvedEvent);
  }

  /**
   * Prepares the list of the reported questions.
   */
  public List<MappedReportedQuestion> reportedQuestions() {
    return reportedQuestionRepository.findAllNotResolved()
        .stream()
        .map(this::toMappedReportedQuestion)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  MappedReportedQuestion toMappedReportedQuestion(ReportedQuestion reportedQuestion) {
    Optional<PreparedQuestion> optionalQuestion =
        questionManager.getById(reportedQuestion.getQuestionId());

    if (optionalQuestion.isPresent()) {
      MappedReportedQuestion mappedReportedQuestion = new MappedReportedQuestion();
      mappedReportedQuestion.setId(reportedQuestion.getId());
      mappedReportedQuestion.setQuestion(optionalQuestion.get());
      mappedReportedQuestion.setReason(reportedQuestion.getReportingReason().getReason());
      mappedReportedQuestion.setDetail(reportedQuestion.getDetail());
      return mappedReportedQuestion;
    }

    // almost sure this will not happen, fix later
    return null;
  }
}
