package com.mks.api.response;

public class InvalidCommandSelectionException extends CommandException {
   public InvalidCommandSelectionException() {
   }

   public InvalidCommandSelectionException(String msg) {
      super(msg);
   }

   public InvalidCommandSelectionException(Throwable t) {
      super(t);
   }

   public String getSelection() {
      if (this.contains("selection")) {
         Field f = this.getField("selection");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
