package com.mks.api.response;

public class CommandException extends APIException {
   public CommandException() {
   }

   public CommandException(String msg) {
      super(msg);
   }

   public CommandException(Throwable t) {
      super(t);
   }
}
