package com.todarch.wisitbe.domain.question;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserQuestionRepository extends JpaRepository<UserQuestion, String> {
  List<UserQuestion> findAllByUserId(String userId);

  @Transactional
  default void deleteAllByQuestionId(String questionId) {
    deleteAllByQuestion_Id(questionId);
  }

  @Transactional
  void deleteAllByQuestion_Id(String questionId);

  /**
   * Gets next question for user.
   * If reaction to an answer is delay, we might end up asking same question.
   * Add randomness for now.
   */
  default Optional<UserQuestion> nextFor(String userId) {
    List<UserQuestion> userQuestions = findTop10ByUserIdOrderByWeightDescLastAskedAtDesc(userId);
    if (userQuestions.isEmpty()) {
      return Optional.empty();
    }
    // avoid asking same question again if updating is slow
    int randIndex = ThreadLocalRandom.current().nextInt(userQuestions.size());
    return Optional.of(userQuestions.get(randIndex));
  }

  // most weighted should be asked first
  // most recently asked one should be asked first
  List<UserQuestion> findTop10ByUserIdOrderByWeightDescLastAskedAtDesc(String userId);

  default List<UserQuestion> next10(String userId) {
    return findTop10ByUserIdOrderByWeightDescLastAskedAtDesc(userId);
  }

  /**
   * Gets user question by id and user id.
   */
  default UserQuestion getByIdAndUserId(String id, String userId) {
    return findByIdAndUserId(id, userId)
        .orElseThrow(() ->
            new ResourceNotFoundException("UserQuestion not found for id/userId"));
  }


  Optional<UserQuestion> findByIdAndUserId(String id, String userId);
}
