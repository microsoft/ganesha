package com.microsoft.ganesha.exception;

public class SemanticKernelException extends RuntimeException {

    SemanticKernelException(String prompt) {
      super("Error running prompt " + prompt);
    }
  }
