package com.mks.api.response;

public class ItemNotFoundException extends ItemException {
   public ItemNotFoundException() {
   }

   public ItemNotFoundException(String msg) {
      super(msg);
   }

   public ItemNotFoundException(Throwable t) {
      super(t);
   }
}
