package com.mks.api.response;

public class APIConnectionException extends APIException {
   public APIConnectionException() {
   }

   public APIConnectionException(String msg) {
      super(msg);
   }

   public APIConnectionException(Throwable t) {
      super(t);
   }
}
