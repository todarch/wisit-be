package com.todarch.wisitbe.infrastructure.rest.errorhandling;

public class DuplicateResourceException extends RuntimeException {
  public DuplicateResourceException(String message) {
    super(message);
  }
}
