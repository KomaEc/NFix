package com.mks.api.response;

public class NoCredentialsException extends ApplicationConnectionException {
   public NoCredentialsException() {
   }

   public NoCredentialsException(String msg) {
      super(msg);
   }

   public NoCredentialsException(Throwable t) {
      super(t);
   }
}
