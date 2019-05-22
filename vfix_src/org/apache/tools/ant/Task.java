package org.apache.tools.ant;

import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.dispatch.DispatchUtils;

public abstract class Task extends ProjectComponent {
   /** @deprecated */
   protected Target target;
   /** @deprecated */
   protected String taskName;
   /** @deprecated */
   protected String taskType;
   /** @deprecated */
   protected RuntimeConfigurable wrapper;
   private boolean invalid;
   private UnknownElement replacement;

   public void setOwningTarget(Target target) {
      this.target = target;
   }

   public Target getOwningTarget() {
      return this.target;
   }

   public void setTaskName(String name) {
      this.taskName = name;
   }

   public String getTaskName() {
      return this.taskName;
   }

   public void setTaskType(String type) {
      this.taskType = type;
   }

   public void init() throws BuildException {
   }

   public void execute() throws BuildException {
   }

   public RuntimeConfigurable getRuntimeConfigurableWrapper() {
      if (this.wrapper == null) {
         this.wrapper = new RuntimeConfigurable(this, this.getTaskName());
      }

      return this.wrapper;
   }

   public void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
      this.wrapper = wrapper;
   }

   public void maybeConfigure() throws BuildException {
      if (!this.invalid) {
         if (this.wrapper != null) {
            this.wrapper.maybeConfigure(this.getProject());
         }
      } else {
         this.getReplacement();
      }

   }

   public void reconfigure() {
      if (this.wrapper != null) {
         this.wrapper.reconfigure(this.getProject());
      }

   }

   protected void handleOutput(String output) {
      this.log((String)output, 2);
   }

   protected void handleFlush(String output) {
      this.handleOutput(output);
   }

   protected int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.getProject().defaultInput(buffer, offset, length);
   }

   protected void handleErrorOutput(String output) {
      this.log((String)output, 1);
   }

   protected void handleErrorFlush(String output) {
      this.handleErrorOutput(output);
   }

   public void log(String msg) {
      this.log((String)msg, 2);
   }

   public void log(String msg, int msgLevel) {
      if (this.getProject() != null) {
         this.getProject().log(this, msg, msgLevel);
      } else {
         super.log(msg, msgLevel);
      }

   }

   public void log(Throwable t, int msgLevel) {
      if (t != null) {
         this.log(t.getMessage(), t, msgLevel);
      }

   }

   public void log(String msg, Throwable t, int msgLevel) {
      if (this.getProject() != null) {
         this.getProject().log(this, msg, t, msgLevel);
      } else {
         super.log(msg, msgLevel);
      }

   }

   public final void perform() {
      if (!this.invalid) {
         this.getProject().fireTaskStarted(this);
         Object reason = null;

         try {
            this.maybeConfigure();
            DispatchUtils.execute(this);
         } catch (BuildException var9) {
            if (var9.getLocation() == Location.UNKNOWN_LOCATION) {
               var9.setLocation(this.getLocation());
            }

            reason = var9;
            throw var9;
         } catch (Exception var10) {
            reason = var10;
            BuildException be = new BuildException(var10);
            be.setLocation(this.getLocation());
            throw be;
         } catch (Error var11) {
            reason = var11;
            throw var11;
         } finally {
            this.getProject().fireTaskFinished(this, (Throwable)reason);
         }
      } else {
         UnknownElement ue = this.getReplacement();
         Task task = ue.getTask();
         task.perform();
      }

   }

   final void markInvalid() {
      this.invalid = true;
   }

   protected final boolean isInvalid() {
      return this.invalid;
   }

   private UnknownElement getReplacement() {
      if (this.replacement == null) {
         this.replacement = new UnknownElement(this.taskType);
         this.replacement.setProject(this.getProject());
         this.replacement.setTaskType(this.taskType);
         this.replacement.setTaskName(this.taskName);
         this.replacement.setLocation(this.location);
         this.replacement.setOwningTarget(this.target);
         this.replacement.setRuntimeConfigurableWrapper(this.wrapper);
         this.wrapper.setProxy(this.replacement);
         this.replaceChildren(this.wrapper, this.replacement);
         this.target.replaceChild(this, (Task)this.replacement);
         this.replacement.maybeConfigure();
      }

      return this.replacement;
   }

   private void replaceChildren(RuntimeConfigurable wrapper, UnknownElement parentElement) {
      Enumeration e = wrapper.getChildren();

      while(e.hasMoreElements()) {
         RuntimeConfigurable childWrapper = (RuntimeConfigurable)e.nextElement();
         UnknownElement childElement = new UnknownElement(childWrapper.getElementTag());
         parentElement.addChild(childElement);
         childElement.setProject(this.getProject());
         childElement.setRuntimeConfigurableWrapper(childWrapper);
         childWrapper.setProxy(childElement);
         this.replaceChildren(childWrapper, childElement);
      }

   }

   public String getTaskType() {
      return this.taskType;
   }

   protected RuntimeConfigurable getWrapper() {
      return this.wrapper;
   }

   public final void bindToOwner(Task owner) {
      this.setProject(owner.getProject());
      this.setOwningTarget(owner.getOwningTarget());
      this.setTaskName(owner.getTaskName());
      this.setDescription(owner.getDescription());
      this.setLocation(owner.getLocation());
      this.setTaskType(owner.getTaskType());
   }
}
