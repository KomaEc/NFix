package org.jboss.util;

public class NotImplementedException extends RuntimeException {
   private static final long serialVersionUID = -3915801189311749818L;

   public NotImplementedException(String msg) {
      super(msg);
   }

   public NotImplementedException() {
   }
}
