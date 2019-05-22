package org.apache.tools.ant.util;

import org.apache.tools.ant.Task;

public final class TaskLogger {
   private Task task;

   public TaskLogger(Task task) {
      this.task = task;
   }

   public void info(String message) {
      this.task.log((String)message, 2);
   }

   public void error(String message) {
      this.task.log((String)message, 0);
   }

   public void warning(String message) {
      this.task.log((String)message, 1);
   }

   public void verbose(String message) {
      this.task.log((String)message, 3);
   }

   public void debug(String message) {
      this.task.log((String)message, 4);
   }
}
