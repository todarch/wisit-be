package com.todarch.wisitbe.infrastructure.rest.errorhandling;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NullPointerException.class)
  protected ResponseEntity<Object> handleNpe(NullPointerException ex) {
    log.error("npe: ", ex);
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    apiError.setMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
    log.error("rte: ", ex);
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    apiError.setMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(InternalApplicationException.class)
  protected ResponseEntity<Object> handleInternalAppEx(InternalApplicationException ex) {
    log.error("internal exception: ", ex);
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(InvalidInputException.class)
  protected ResponseEntity<Object> handleInvalidInputEx(InvalidInputException ex) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
    apiError.setMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles exception thrown by @Valid.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    log.error("validation exception: ", ex);
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
    apiError.setMessage("the request is not valid");
    apiError.setValidationErrors(toValidationErrors(ex));
    return buildResponseEntity(apiError);
  }

  private Map<String, String> toValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
    apiError.setMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

}

