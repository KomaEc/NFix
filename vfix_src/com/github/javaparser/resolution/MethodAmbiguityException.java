package com.github.javaparser.resolution;

public class MethodAmbiguityException extends RuntimeException {
   public MethodAmbiguityException(String description) {
      super(description);
   }
}
