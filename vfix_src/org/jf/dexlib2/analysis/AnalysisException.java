package org.jf.dexlib2.analysis;

import org.jf.util.ExceptionWithContext;

public class AnalysisException extends ExceptionWithContext {
   public int codeAddress;

   public AnalysisException(Throwable cause) {
      super(cause);
   }

   public AnalysisException(Throwable cause, String message, Object... formatArgs) {
      super(cause, message, formatArgs);
   }

   public AnalysisException(String message, Object... formatArgs) {
      super(message, formatArgs);
   }
}
