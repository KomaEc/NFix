package com.mks.api.response;

public class InvalidCommandRunnerStateException extends CommandException {
   public InvalidCommandRunnerStateException() {
   }

   public InvalidCommandRunnerStateException(String msg) {
      super(msg);
   }

   public InvalidCommandRunnerStateException(Throwable t) {
      super(t);
   }
}
