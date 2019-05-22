package com.mks.api.response;

public class ApplicationConnectionException extends ApplicationException {
   public ApplicationConnectionException() {
   }

   public ApplicationConnectionException(String msg) {
      super(msg);
   }

   public ApplicationConnectionException(Throwable t) {
      super(t);
   }
}
