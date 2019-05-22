package com.mks.api.response;

public class APIInternalError extends APIError {
   public APIInternalError() {
   }

   public APIInternalError(String msg) {
      super(msg);
   }

   public APIInternalError(Throwable t) {
      super(t);
   }
}
