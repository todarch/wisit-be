package com.todarch.wisitbe.domain.question;

import com.todarch.wisitbe.domain.user.User;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserQuestionPickingSpecification {

  /**
   * Defines when the system should pick new questions for the user.
   */
  public boolean isSatisfiedBy(User user, List<UserQuestion> nextBatchQuestions) {

    if (CollectionUtils.isEmpty(nextBatchQuestions)) {
      return true;
    }

    UserQuestion nextQuestion = nextBatchQuestions.get(0);

    if (!nextQuestion.isNew()) {
      return user.isEligibleForMoreQuestions();
    }

    return false;
  }
}
