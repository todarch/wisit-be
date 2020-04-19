package com.todarch.wisitbe.infrastructure.rest.errorhandling;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter(AccessLevel.PROTECTED)
@Getter
class ApiError {
  private HttpStatus status;
  private LocalDateTime timestamp;
  private String message;
  private Map<String, String> validationErrors;

  ApiError(HttpStatus status, Throwable ex) {
    this(status, "Unexpected error", ex);
  }

  ApiError(HttpStatus status, String message, Throwable ex) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
  }
}
