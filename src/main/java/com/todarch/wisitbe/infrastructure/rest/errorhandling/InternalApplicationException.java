package com.todarch.wisitbe.infrastructure.rest.errorhandling;

public class InternalApplicationException extends RuntimeException {
  public InternalApplicationException(String message) {
    super(message);
  }
}
