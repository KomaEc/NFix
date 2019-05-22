package com.mks.api.response;

public class InvalidIntegrationPointException extends APIConnectionException {
   public InvalidIntegrationPointException() {
   }

   public InvalidIntegrationPointException(String msg) {
      super(msg);
   }

   public InvalidIntegrationPointException(Throwable t) {
      super(t);
   }
}
