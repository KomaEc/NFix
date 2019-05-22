package com.mks.api.response;

public class ItemException extends ApplicationException {
   public ItemException() {
   }

   public ItemException(String msg) {
      super(msg);
   }

   public ItemException(Throwable t) {
      super(t);
   }

   public String getId() {
      if (this.contains("item-id")) {
         Field f = this.getField("item-id");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }

   public String getModelType() {
      if (this.contains("item-modelType")) {
         Field f = this.getField("item-modelType");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
