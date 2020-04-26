package com.todarch.wisitbe.domain.question;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReactionRepository extends JpaRepository<QuestionReaction, Long> {

  Optional<QuestionReaction> findByUserIdAndQuestionId(String userId, String questionId);


  default long countLikesByQuestionId(String questionId) {
    return countByLikedAndQuestionId(true, questionId);
  }

  default long countDislikesByQuestionId(String questionId) {
    return countByLikedAndQuestionId(false, questionId);
  }

  long countByLikedAndQuestionId(boolean liked, String questionId);

}
