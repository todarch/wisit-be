package com.todarch.wisitbe.infrastructure.security;

import com.todarch.wisitbe.domain.user.NeedsPickedUsernameException;
import com.todarch.wisitbe.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class CurrentUser {
  private final User user;

  public String id() {
    return this.user.getId();
  }

  public void requirePickedUsername() {
    user.pickedUsername().orElseThrow(NeedsPickedUsernameException::new);
  }
}
