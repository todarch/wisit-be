package com.todarch.wisitbe.domain.reporting;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedQuestionRepository extends JpaRepository<ReportedQuestion, Long> {

  List<ReportedQuestion> findAllByResolved(boolean resolved);

  default List<ReportedQuestion> findAllNotResolved() {
    return findAllByResolved(false);
  }
}
