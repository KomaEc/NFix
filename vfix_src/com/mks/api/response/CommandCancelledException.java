package com.mks.api.response;

public class CommandCancelledException extends CommandException {
   public CommandCancelledException() {
   }

   public CommandCancelledException(String msg) {
      super(msg);
   }

   public CommandCancelledException(Throwable t) {
      super(t);
   }
}
