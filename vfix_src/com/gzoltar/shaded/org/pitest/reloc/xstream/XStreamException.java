package com.gzoltar.shaded.org.pitest.reloc.xstream;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.BaseException;

public class XStreamException extends BaseException {
   private Throwable cause;

   protected XStreamException() {
      this("", (Throwable)null);
   }

   public XStreamException(String message) {
      this(message, (Throwable)null);
   }

   public XStreamException(Throwable cause) {
      this("", cause);
   }

   public XStreamException(String message, Throwable cause) {
      super(message + (cause == null ? "" : " : " + cause.getMessage()));
      this.cause = cause;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
