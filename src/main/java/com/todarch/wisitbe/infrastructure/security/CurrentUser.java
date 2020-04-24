package com.todarch.wisitbe.infrastructure.security;

import com.todarch.wisitbe.domain.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CurrentUser {
  private final User user;

  public String id() {
    return this.user.getId();
  }
}
