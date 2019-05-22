package org.jboss.util.threadpool;

public class TaskStoppedException extends RuntimeException {
   private static final long serialVersionUID = -5166095239829218680L;

   public TaskStoppedException() {
   }

   public TaskStoppedException(String message) {
      super(message);
   }
}
