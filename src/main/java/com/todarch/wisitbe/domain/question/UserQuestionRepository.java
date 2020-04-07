package com.todarch.wisitbe.domain.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, String> {
  List<UserQuestion> findAllByUserId(String userId);
}
