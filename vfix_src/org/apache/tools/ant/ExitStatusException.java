package org.apache.tools.ant;

public class ExitStatusException extends BuildException {
   private int status;

   public ExitStatusException(int status) {
      this.status = status;
   }

   public ExitStatusException(String msg, int status) {
      super(msg);
      this.status = status;
   }

   public ExitStatusException(String message, int status, Location location) {
      super(message, location);
      this.status = status;
   }

   public int getStatus() {
      return this.status;
   }
}
