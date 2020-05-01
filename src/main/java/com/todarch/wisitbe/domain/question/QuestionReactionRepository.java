package com.todarch.wisitbe.domain.question;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReactionRepository extends JpaRepository<QuestionReaction, Long> {

  Optional<QuestionReaction> findByUserIdAndQuestionId(String userId, String questionId);

  Optional<QuestionReaction> findByUserIdAndQuestionIdAndLiked(String userId,
                                                               String questionId,
                                                               boolean liked);

  default Optional<QuestionReaction> findLikeByUserIdAndQuestionId(String userId,
                                                                      String questionId) {
    return findByUserIdAndQuestionIdAndLiked(userId, questionId, true);
  }

  default Optional<QuestionReaction> findDislikeByUserIdAndQuestionId(String userId,
                                                                      String questionId) {
    return findByUserIdAndQuestionIdAndLiked(userId, questionId, false);
  }

  default long countLikesByQuestionId(String questionId) {
    return countByLikedAndQuestionId(true, questionId);
  }

  default long countDislikesByQuestionId(String questionId) {
    return countByLikedAndQuestionId(false, questionId);
  }

  long countByLikedAndQuestionId(boolean liked, String questionId);

}
