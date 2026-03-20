package com.project_management_system.user_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(
          UserNotFoundException ex,
          HttpServletRequest request){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
          UserAlreadyExistsException ex,
          HttpServletRequest request
  ){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
  }

  //Handle BadCredentialsException(Wrong Password)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
          BadCredentialsException ex,
          HttpServletRequest request){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .message("Invalid username or password!")
            .path(request.getRequestURI())
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
  }

  //Handle UsernameNotFoundException
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
          UsernameNotFoundException ex,
          HttpServletRequest request
  ){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
  }
  //Handle Validation Errors
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
          MethodArgumentNotValidException ex,
          HttpServletRequest request
  ){
    Map<String,String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error)->{
      String fieldName =((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName,errorMessage);
    });
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Validation failed")
            .path(request.getRequestURI())
            .validationErrors(validationErrors)
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
  }

  // Handle All Other Exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
          Exception ex,
          HttpServletRequest request
  ){
    ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("An unexpected error occurred: "+ex.getMessage())
            .path(request.getRequestURI())
            .timeStamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
  }

}