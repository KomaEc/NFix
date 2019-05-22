package com.mks.api.response.impl;

public class CommandException extends Exception {
   public static final int VALUE_TOO_BIG = 202;
   public static final int UNKNOWN_EXCEPTION = 203;
   public static final int API_EXCEPTION = 204;
   private int returnCode;

   public CommandException(String msg) {
      this(203, (String)msg);
   }

   public CommandException(Throwable cause) {
      this(203, (Throwable)cause);
   }

   public CommandException(int returnCode, String msg) {
      super(msg);
      this.returnCode = returnCode;
   }

   public CommandException(int returnCode, Throwable cause) {
      super(cause.getLocalizedMessage());
      this.returnCode = returnCode;
   }

   public int getReturnCode() {
      return this.returnCode;
   }
}
