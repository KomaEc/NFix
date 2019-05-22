package com.mks.api.response;

public class ApplicationException extends APIException {
   public ApplicationException() {
   }

   public ApplicationException(String msg) {
      super(msg);
   }

   public ApplicationException(Throwable t) {
      super(t);
   }
}
