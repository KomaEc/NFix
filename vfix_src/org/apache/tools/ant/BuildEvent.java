package org.apache.tools.ant;

import java.util.EventObject;

public class BuildEvent extends EventObject {
   private Project project;
   private Target target;
   private Task task;
   private String message;
   private int priority = 3;
   private Throwable exception;

   public BuildEvent(Project project) {
      super(project);
      this.project = project;
      this.target = null;
      this.task = null;
   }

   public BuildEvent(Target target) {
      super(target);
      this.project = target.getProject();
      this.target = target;
      this.task = null;
   }

   public BuildEvent(Task task) {
      super(task);
      this.project = task.getProject();
      this.target = task.getOwningTarget();
      this.task = task;
   }

   public void setMessage(String message, int priority) {
      this.message = message;
      this.priority = priority;
   }

   public void setException(Throwable exception) {
      this.exception = exception;
   }

   public Project getProject() {
      return this.project;
   }

   public Target getTarget() {
      return this.target;
   }

   public Task getTask() {
      return this.task;
   }

   public String getMessage() {
      return this.message;
   }

   public int getPriority() {
      return this.priority;
   }

   public Throwable getException() {
      return this.exception;
   }
}
