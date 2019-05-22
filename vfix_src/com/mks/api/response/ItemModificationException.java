package com.mks.api.response;

public class ItemModificationException extends ItemException {
   public ItemModificationException() {
   }

   public ItemModificationException(String msg) {
      super(msg);
   }

   public ItemModificationException(Throwable t) {
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
