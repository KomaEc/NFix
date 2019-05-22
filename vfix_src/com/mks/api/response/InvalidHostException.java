package com.mks.api.response;

public class InvalidHostException extends APIConnectionException {
   public InvalidHostException() {
   }

   public InvalidHostException(String msg) {
      super(msg);
   }

   public InvalidHostException(Throwable t) {
      super(t);
   }
}
