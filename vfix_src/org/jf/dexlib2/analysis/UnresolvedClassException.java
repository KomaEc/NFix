package org.jf.dexlib2.analysis;

import org.jf.util.ExceptionWithContext;

public class UnresolvedClassException extends ExceptionWithContext {
   public UnresolvedClassException(Throwable cause) {
      super(cause);
   }

   public UnresolvedClassException(Throwable cause, String message, Object... formatArgs) {
      super(cause, message, formatArgs);
   }

   public UnresolvedClassException(String message, Object... formatArgs) {
      super(message, formatArgs);
   }
}
