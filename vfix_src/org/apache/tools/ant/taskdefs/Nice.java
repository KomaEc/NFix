package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Nice extends Task {
   private Integer newPriority;
   private String currentPriority;

   public void execute() throws BuildException {
      Thread self = Thread.currentThread();
      int priority = self.getPriority();
      if (this.currentPriority != null) {
         String current = Integer.toString(priority);
         this.getProject().setNewProperty(this.currentPriority, current);
      }

      if (this.newPriority != null && priority != this.newPriority) {
         try {
            self.setPriority(this.newPriority);
         } catch (SecurityException var4) {
            this.log("Unable to set new priority -a security manager is in the way", 1);
         } catch (IllegalArgumentException var5) {
            throw new BuildException("Priority out of range", var5);
         }
      }

   }

   public void setCurrentPriority(String currentPriority) {
      this.currentPriority = currentPriority;
   }

   public void setNewPriority(int newPriority) {
      if (newPriority >= 1 && newPriority <= 10) {
         this.newPriority = new Integer(newPriority);
      } else {
         throw new BuildException("The thread priority is out of the range 1-10");
      }
   }
}
