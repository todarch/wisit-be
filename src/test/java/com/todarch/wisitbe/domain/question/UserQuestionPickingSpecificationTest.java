package com.todarch.wisitbe.domain.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.todarch.wisitbe.data.UserData;
import com.todarch.wisitbe.data.UserQuestionData;
import com.todarch.wisitbe.domain.user.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserQuestionPickingSpecificationTest {

  private UserQuestionPickingSpecification userQuestionPickingSpecification =
      new UserQuestionPickingSpecification();

  @Test
  void triggersUserQuestionPickingIfUserHasNoNextQuestion() {
    User testUser = UserData.newUser();

    boolean satisfiedBy =
        userQuestionPickingSpecification.isSatisfiedBy(testUser, Collections.emptyList());

    assertThat(satisfiedBy).isTrue();
  }

  @Test
  void triggersUserQuestionPickingIfNextQuestionIsNotANewOne() {
    User testUser = UserData.newUser();

    UserQuestion alreadyAnsweredQuestion = UserQuestionData.newUserQuestionFor(testUser);
    alreadyAnsweredQuestion.answer(5);

    boolean satisfiedBy =
        userQuestionPickingSpecification.isSatisfiedBy(testUser, List.of(alreadyAnsweredQuestion));

    assertThat(satisfiedBy).isTrue();
  }

  @Test
  void doesNotTriggerUserQuestionPickingIfPreviousPickingForUserWasTooRecently() {
    User testUser = UserData.newUser();
    testUser.setPivotPoint(LocalDateTime.now().minusMinutes(15));

    UserQuestion alreadyAnsweredQuestion = UserQuestionData.newUserQuestionFor(testUser);
    alreadyAnsweredQuestion.answer(5);

    boolean satisfiedBy =
        userQuestionPickingSpecification.isSatisfiedBy(testUser, List.of(alreadyAnsweredQuestion));

    assertThat(satisfiedBy).isFalse();
  }

}
