package com.todarch.wisitbe.domain.user;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.InvalidInputException;

public class NeedsPickedUsernameException extends InvalidInputException {
  public NeedsPickedUsernameException() {
    super("User needs to pick a username");
  }

  public NeedsPickedUsernameException(String message) {
    super(message);
  }
}
