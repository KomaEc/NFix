package com.mks.api.response;

public class ApplicationInternalException extends ApplicationException {
   public ApplicationInternalException() {
   }

   public ApplicationInternalException(String msg) {
      super(msg);
   }

   public ApplicationInternalException(Throwable t) {
      super(t);
   }
}
