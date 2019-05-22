package com.mks.api.response;

public class PermissionException extends ApplicationException {
   public PermissionException() {
   }

   public PermissionException(String msg) {
      super(msg);
   }

   public PermissionException(Throwable t) {
      super(t);
   }
}
