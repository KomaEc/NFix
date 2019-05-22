package com.mks.api.response;

public class InvalidCommandOptionException extends CommandException {
   public InvalidCommandOptionException() {
   }

   public InvalidCommandOptionException(String msg) {
      super(msg);
   }

   public InvalidCommandOptionException(Throwable t) {
      super(t);
   }

   public String getOption() {
      if (this.contains("command-option")) {
         Field f = this.getField("command-option");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
