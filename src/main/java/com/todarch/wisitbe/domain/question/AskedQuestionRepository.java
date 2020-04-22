package com.todarch.wisitbe.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AskedQuestionRepository extends JpaRepository<AskedQuestion, Long> {
  long countByQuestionId(String questionId);
}
