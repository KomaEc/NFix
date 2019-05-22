package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

/** @deprecated */
public abstract class BaseException extends RuntimeException {
   protected BaseException(String message) {
      super(message);
   }

   public abstract Throwable getCause();
}
