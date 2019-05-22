package com.mks.api.response;

public class UnknownCommandException extends CommandException {
   public UnknownCommandException() {
   }

   public UnknownCommandException(String msg) {
      super(msg);
   }

   public UnknownCommandException(Throwable t) {
      super(t);
   }

   public String getCommand() {
      if (this.contains("command")) {
         Field f = this.getField("command");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
