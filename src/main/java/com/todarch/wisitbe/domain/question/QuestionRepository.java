package com.todarch.wisitbe.domain.question;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, String> {

  List<Question> findTop20ByActiveAndCreatedAtAfterOrderByCreatedAtAsc(boolean active,
                                                                       LocalDateTime createdAt);

  default List<Question> pick20QuestionsCreatedAfter(LocalDateTime createdAt) {
    return findTop20ByActiveAndCreatedAtAfterOrderByCreatedAtAsc(true, createdAt);
  }

  default Question tryToFindById(String id) {
    return findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
  }

  default List<Question> findAllActive() {
    return findAllByActive(true);
  }

  List<Question> findAllByActive(boolean active);


}
