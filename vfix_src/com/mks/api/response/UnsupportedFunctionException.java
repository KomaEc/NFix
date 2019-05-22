package com.mks.api.response;

public class UnsupportedFunctionException extends CommandException {
   public UnsupportedFunctionException() {
   }

   public UnsupportedFunctionException(String msg) {
      super(msg);
   }

   public UnsupportedFunctionException(Throwable t) {
      super(t);
   }
}
