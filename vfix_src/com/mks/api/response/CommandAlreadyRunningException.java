package com.mks.api.response;

public class CommandAlreadyRunningException extends CommandException {
   public CommandAlreadyRunningException() {
   }

   public CommandAlreadyRunningException(String msg) {
      super(msg);
   }

   CommandAlreadyRunningException(Throwable t) {
      super(t);
   }
}
