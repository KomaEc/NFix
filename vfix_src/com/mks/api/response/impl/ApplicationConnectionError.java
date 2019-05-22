package com.mks.api.response.impl;

import com.mks.api.response.APIError;

public class ApplicationConnectionError extends APIError {
   public ApplicationConnectionError() {
   }

   public ApplicationConnectionError(String msg) {
      super(msg);
   }

   public ApplicationConnectionError(Throwable t) {
      super(t);
   }
}
