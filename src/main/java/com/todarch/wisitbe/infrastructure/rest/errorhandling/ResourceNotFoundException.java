package com.todarch.wisitbe.infrastructure.rest.errorhandling;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
