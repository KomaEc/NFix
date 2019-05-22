package org.apache.tools.ant.taskdefs;

import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

public class Sequential extends Task implements TaskContainer {
   private Vector nestedTasks = new Vector();

   public void addTask(Task nestedTask) {
      this.nestedTasks.addElement(nestedTask);
   }

   public void execute() throws BuildException {
      Iterator i = this.nestedTasks.iterator();

      while(i.hasNext()) {
         Task nestedTask = (Task)i.next();
         nestedTask.perform();
      }

   }
}
