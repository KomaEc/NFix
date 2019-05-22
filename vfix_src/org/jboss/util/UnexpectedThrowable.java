package org.jboss.util;

public class UnexpectedThrowable extends NestedError {
   private static final long serialVersionUID = 4318032849691437298L;

   public UnexpectedThrowable(String msg) {
      super(msg);
   }

   public UnexpectedThrowable(String msg, Throwable nested) {
      super(msg, nested);
   }

   public UnexpectedThrowable(Throwable nested) {
      super(nested);
   }

   public UnexpectedThrowable() {
   }
}
