package com.mks.api.response;

public class IncompatibleVersionException extends ApplicationConnectionException {
   public IncompatibleVersionException() {
   }

   public IncompatibleVersionException(String msg) {
      super(msg);
   }

   public IncompatibleVersionException(Throwable t) {
      super(t);
   }
}
