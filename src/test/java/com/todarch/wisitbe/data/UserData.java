package com.todarch.wisitbe.data;

import com.todarch.wisitbe.domain.user.User;
import java.util.UUID;

public class UserData {

  /**
   * New user constructor method.
   */
  public static User newUser() {
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setUsername("TEST_USER_USERNAME");
    user.setIp("TEST_USER_IP");
    user.setUserAgent("TEST_USER_AGENT");
    return user;
  }

}
