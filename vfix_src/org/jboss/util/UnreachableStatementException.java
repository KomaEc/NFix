package org.jboss.util;

public class UnreachableStatementException extends RuntimeException {
   private static final long serialVersionUID = -6741968131968754812L;

   public UnreachableStatementException(String msg) {
      super(msg);
   }

   public UnreachableStatementException() {
   }
}
