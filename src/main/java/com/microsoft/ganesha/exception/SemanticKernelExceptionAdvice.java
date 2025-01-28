package com.microsoft.ganesha.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SemanticKernelExceptionAdvice {

  @ExceptionHandler(SemanticKernelException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String semanticKernelExceptionHandler(SemanticKernelException ex) {
    return ex.getMessage();
  }
}