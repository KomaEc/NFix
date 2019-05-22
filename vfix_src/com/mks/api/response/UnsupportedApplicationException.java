package com.mks.api.response;

public class UnsupportedApplicationException extends ApplicationException {
   public UnsupportedApplicationException() {
   }

   public UnsupportedApplicationException(String msg) {
      super(msg);
   }

   public UnsupportedApplicationException(Throwable t) {
      super(t);
   }

   public String getApplicationName() {
      if (this.contains("applicationName")) {
         Field f = this.getField("applicationName");
         if (f.getValue() != null) {
            return f.getValue().toString();
         }
      }

      return null;
   }
}
