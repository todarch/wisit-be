package com.todarch.wisitbe.domain.user;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void cannotChangeUsername() {
    User user = new User();
    user.setUsername("first-time");
    Assertions.assertThatThrownBy(() -> user.setUsername("second-time"))
        .isInstanceOf(CannotChangeUserNameException.class);
  }

  @Test
  void providesDefaultValueAsMinForPivotDateIfNull() {
    User user = new User();
    Assertions.assertThat(user.pivotPoint()).isEqualTo(User.MIN_CREATED_AT_VALUE);
  }

  @Test
  void canUpdatePivotDate() {
    User user = new User();
    LocalDateTime createdAt = LocalDateTime.now();
    user.setPivotPoint(createdAt);
    Assertions.assertThat(user.pivotPoint()).isEqualTo(createdAt);
  }
}
