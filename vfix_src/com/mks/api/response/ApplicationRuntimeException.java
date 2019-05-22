package com.mks.api.response;

public class ApplicationRuntimeException extends ApplicationException {
   private Response response;

   public ApplicationRuntimeException() {
   }

   public ApplicationRuntimeException(String msg) {
      super(msg);
   }

   public ApplicationRuntimeException(Throwable t) {
      super(t);
   }
}
