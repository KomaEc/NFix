package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

public class WrappedCheckedException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public WrappedCheckedException(String message, Throwable cause) {
      super(message, cause);
   }

   public WrappedCheckedException(Throwable cause) {
      super(cause);
   }
}
