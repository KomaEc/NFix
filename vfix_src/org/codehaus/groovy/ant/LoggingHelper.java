package org.codehaus.groovy.ant;

import org.apache.tools.ant.Task;

public class LoggingHelper {
   private Task owner;

   public LoggingHelper(Task owner) {
      assert owner != null;

      this.owner = owner;
   }

   public void error(String msg) {
      this.owner.log((String)msg, 0);
   }

   public void warn(String msg) {
      this.owner.log((String)msg, 1);
   }

   public void info(String msg) {
      this.owner.log((String)msg, 2);
   }

   public void verbose(String msg) {
      this.owner.log((String)msg, 3);
   }

   public void debug(String msg) {
      this.owner.log((String)msg, 4);
   }
}
