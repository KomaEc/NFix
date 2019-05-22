package com.mks.api.response;

public class InterruptedException extends APIException {
   public InterruptedException() {
   }

   public InterruptedException(String msg) {
      super(msg);
   }

   public InterruptedException(Throwable t) {
      super(t);
   }
}
