package com.mks.api.response.impl;

public class InvalidDirectiveException extends CommandException {
   private static final int ERROR_CODE = 206;

   public InvalidDirectiveException(String msg) {
      super(206, (String)msg);
   }

   public InvalidDirectiveException(Throwable cause) {
      super(206, (Throwable)cause);
   }
}
