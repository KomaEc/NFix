package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;

public class ConditionTask extends ConditionBase {
   private String property = null;
   private String value = "true";
   private String alternative = null;

   public ConditionTask() {
      super("condition");
   }

   public void setProperty(String p) {
      this.property = p;
   }

   public void setValue(String v) {
      this.value = v;
   }

   public void setElse(String e) {
      this.alternative = e;
   }

   public void execute() throws BuildException {
      if (this.countConditions() > 1) {
         throw new BuildException("You must not nest more than one condition into <" + this.getTaskName() + ">");
      } else if (this.countConditions() < 1) {
         throw new BuildException("You must nest a condition into <" + this.getTaskName() + ">");
      } else if (this.property == null) {
         throw new BuildException("The property attribute is required.");
      } else {
         Condition c = (Condition)this.getConditions().nextElement();
         if (c.eval()) {
            this.log("Condition true; setting " + this.property + " to " + this.value, 4);
            this.getProject().setNewProperty(this.property, this.value);
         } else if (this.alternative != null) {
            this.log("Condition false; setting " + this.property + " to " + this.alternative, 4);
            this.getProject().setNewProperty(this.property, this.alternative);
         } else {
            this.log("Condition false; not setting " + this.property, 4);
         }

      }
   }
}
