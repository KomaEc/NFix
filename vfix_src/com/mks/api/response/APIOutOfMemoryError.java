package com.mks.api.response;

public class APIOutOfMemoryError extends APIInternalError {
   public APIOutOfMemoryError() {
   }

   public APIOutOfMemoryError(String msg) {
      super(msg);
   }

   public APIOutOfMemoryError(Throwable t) {
      super(t);
   }
}
