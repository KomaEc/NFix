package org.apache.maven.execution;

public class BuildFailure {
   private final Exception cause;
   private final String task;
   private final long time;

   BuildFailure(Exception cause, String task, long time) {
      this.cause = cause;
      this.task = task;
      this.time = time;
   }

   public String getTask() {
      return this.task;
   }

   public Exception getCause() {
      return this.cause;
   }

   public long getTime() {
      return this.time;
   }
}
