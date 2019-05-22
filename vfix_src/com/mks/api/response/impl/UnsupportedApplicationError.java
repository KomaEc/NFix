package com.mks.api.response.impl;

import com.mks.api.response.APIError;

public class UnsupportedApplicationError extends APIError {
   public UnsupportedApplicationError() {
   }

   public UnsupportedApplicationError(String msg) {
      super(msg);
   }

   public UnsupportedApplicationError(Throwable t) {
      super(t);
   }
}
