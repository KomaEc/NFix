package org.jboss.util;

public class EmptyStringException extends IllegalArgumentException {
   private static final long serialVersionUID = -7958716001355854762L;

   public EmptyStringException(String msg) {
      super(msg);
   }

   public EmptyStringException() {
   }
}
