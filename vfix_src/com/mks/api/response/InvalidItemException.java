package com.mks.api.response;

public class InvalidItemException extends ItemException {
   public InvalidItemException() {
   }

   public InvalidItemException(String msg) {
      super(msg);
   }

   public InvalidItemException(Throwable t) {
      super(t);
   }

   public String getReason() {
      if (this.contains("reason")) {
         Field f = this.getField("reason");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
