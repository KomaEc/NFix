package com.mks.api.response.impl;

public class UnsupportedVersionError extends Error {
   public UnsupportedVersionError() {
   }

   public UnsupportedVersionError(String msg) {
      super(msg);
   }

   public UnsupportedVersionError(Throwable t) {
      super(t);
   }
}
