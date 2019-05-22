package com.mks.api.response;

public class UnknownException extends InternalException {
   public UnknownException() {
   }

   public UnknownException(String msg) {
      super(msg);
   }

   public UnknownException(Throwable t) {
      super(t);
   }
}
