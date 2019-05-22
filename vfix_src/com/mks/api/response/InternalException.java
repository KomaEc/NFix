package com.mks.api.response;

public class InternalException extends APIException {
   public InternalException() {
   }

   public InternalException(String msg) {
      super(msg);
   }

   public InternalException(Throwable t) {
      super(t);
   }
}
