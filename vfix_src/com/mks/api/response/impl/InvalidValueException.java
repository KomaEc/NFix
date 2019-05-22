package com.mks.api.response.impl;

public class InvalidValueException extends CommandException {
   private static final int ERROR_CODE = 208;

   public InvalidValueException(String msg) {
      super(208, (String)msg);
   }

   public InvalidValueException(Throwable cause) {
      super(208, (Throwable)cause);
   }
}
