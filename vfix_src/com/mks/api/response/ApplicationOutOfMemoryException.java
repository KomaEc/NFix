package com.mks.api.response;

public class ApplicationOutOfMemoryException extends ApplicationRuntimeException {
   public ApplicationOutOfMemoryException() {
   }

   public ApplicationOutOfMemoryException(String msg) {
      super(msg);
   }

   public ApplicationOutOfMemoryException(Throwable t) {
      super(t);
   }
}
