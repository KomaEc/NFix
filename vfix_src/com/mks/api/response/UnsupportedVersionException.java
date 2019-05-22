package com.mks.api.response;

public class UnsupportedVersionException extends APIConnectionException {
   public UnsupportedVersionException() {
   }

   public UnsupportedVersionException(String msg) {
      super(msg);
   }

   public UnsupportedVersionException(Throwable t) {
      super(t);
   }
}
