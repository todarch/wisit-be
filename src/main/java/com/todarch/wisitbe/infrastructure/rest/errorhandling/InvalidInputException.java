package com.todarch.wisitbe.infrastructure.rest.errorhandling;

public class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }
}
