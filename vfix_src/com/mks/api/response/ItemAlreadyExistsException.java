package com.mks.api.response;

public class ItemAlreadyExistsException extends ItemException {
   public ItemAlreadyExistsException() {
   }

   public ItemAlreadyExistsException(String msg) {
      super(msg);
   }

   public ItemAlreadyExistsException(Throwable t) {
      super(t);
   }
}
