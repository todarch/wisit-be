package com.todarch.wisitbe.domain.question;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, String> {

  default Question tryToFindById(String id) {
    return findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
  }

  default List<Question> findAllActive() {
    return findAllByActive(true);
  }

  List<Question> findAllByActive(boolean active);


}
