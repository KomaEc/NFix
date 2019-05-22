package org.apache.tools.ant;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BuildException extends RuntimeException {
   private Throwable cause;
   private Location location;

   public BuildException() {
      this.location = Location.UNKNOWN_LOCATION;
   }

   public BuildException(String message) {
      super(message);
      this.location = Location.UNKNOWN_LOCATION;
   }

   public BuildException(String message, Throwable cause) {
      super(message);
      this.location = Location.UNKNOWN_LOCATION;
      this.cause = cause;
   }

   public BuildException(String msg, Throwable cause, Location location) {
      this(msg, cause);
      this.location = location;
   }

   public BuildException(Throwable cause) {
      super(cause.toString());
      this.location = Location.UNKNOWN_LOCATION;
      this.cause = cause;
   }

   public BuildException(String message, Location location) {
      super(message);
      this.location = Location.UNKNOWN_LOCATION;
      this.location = location;
   }

   public BuildException(Throwable cause, Location location) {
      this(cause);
      this.location = location;
   }

   public Throwable getException() {
      return this.cause;
   }

   public Throwable getCause() {
      return this.getException();
   }

   public String toString() {
      return this.location.toString() + this.getMessage();
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public Location getLocation() {
      return this.location;
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream ps) {
      synchronized(ps) {
         super.printStackTrace(ps);
         if (this.cause != null) {
            ps.println("--- Nested Exception ---");
            this.cause.printStackTrace(ps);
         }

      }
   }

   public void printStackTrace(PrintWriter pw) {
      synchronized(pw) {
         super.printStackTrace(pw);
         if (this.cause != null) {
            pw.println("--- Nested Exception ---");
            this.cause.printStackTrace(pw);
         }

      }
   }
}
