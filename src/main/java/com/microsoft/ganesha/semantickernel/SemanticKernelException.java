package com.microsoft.ganesha.semantickernel;

public class SemanticKernelException extends RuntimeException {

    SemanticKernelException(String prompt) {
      super("Error  running prompt " + prompt);
    }
  }
