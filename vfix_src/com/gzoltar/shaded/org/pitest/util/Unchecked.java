package com.gzoltar.shaded.org.pitest.util;

public abstract class Unchecked {
   public static RuntimeException translateCheckedException(Throwable ex) {
      return new PitError(ex.getMessage(), ex);
   }

   public static RuntimeException translateCheckedException(String message, Throwable ex) {
      return new PitError(message, ex);
   }
}
