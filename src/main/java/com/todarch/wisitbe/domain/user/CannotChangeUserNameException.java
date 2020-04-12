package com.todarch.wisitbe.domain.user;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.InvalidInputException;

public class CannotChangeUserNameException extends InvalidInputException {

  public CannotChangeUserNameException(String message) {
    super(message);
  }
}
