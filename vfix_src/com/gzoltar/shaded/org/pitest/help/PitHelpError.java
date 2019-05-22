package com.gzoltar.shaded.org.pitest.help;

public class PitHelpError extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public PitHelpError(Help message, Object... params) {
      super(message.format(params));
   }
}
