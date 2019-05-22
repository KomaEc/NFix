package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitStatusException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;

public class Exit extends Task {
   private String message;
   private String ifCondition;
   private String unlessCondition;
   private Exit.NestedCondition nestedCondition;
   private Integer status;

   public void setMessage(String value) {
      this.message = value;
   }

   public void setIf(String c) {
      this.ifCondition = c;
   }

   public void setUnless(String c) {
      this.unlessCondition = c;
   }

   public void setStatus(int i) {
      this.status = new Integer(i);
   }

   public void execute() throws BuildException {
      boolean fail = this.nestedConditionPresent() ? this.testNestedCondition() : this.testIfCondition() && this.testUnlessCondition();
      if (fail) {
         String text = null;
         if (this.message != null && this.message.trim().length() > 0) {
            text = this.message.trim();
         } else {
            if (this.ifCondition != null && this.ifCondition.length() > 0 && this.getProject().getProperty(this.ifCondition) != null) {
               text = "if=" + this.ifCondition;
            }

            if (this.unlessCondition != null && this.unlessCondition.length() > 0 && this.getProject().getProperty(this.unlessCondition) == null) {
               if (text == null) {
                  text = "";
               } else {
                  text = text + " and ";
               }

               text = text + "unless=" + this.unlessCondition;
            }

            if (this.nestedConditionPresent()) {
               text = "condition satisfied";
            } else if (text == null) {
               text = "No message";
            }
         }

         this.log("failing due to " + text, 4);
         throw this.status == null ? new BuildException(text) : new ExitStatusException(text, this.status);
      }
   }

   public void addText(String msg) {
      if (this.message == null) {
         this.message = "";
      }

      this.message = this.message + this.getProject().replaceProperties(msg);
   }

   public ConditionBase createCondition() {
      if (this.nestedCondition != null) {
         throw new BuildException("Only one nested condition is allowed.");
      } else {
         this.nestedCondition = new Exit.NestedCondition();
         return this.nestedCondition;
      }
   }

   private boolean testIfCondition() {
      if (this.ifCondition != null && !"".equals(this.ifCondition)) {
         return this.getProject().getProperty(this.ifCondition) != null;
      } else {
         return true;
      }
   }

   private boolean testUnlessCondition() {
      if (this.unlessCondition != null && !"".equals(this.unlessCondition)) {
         return this.getProject().getProperty(this.unlessCondition) == null;
      } else {
         return true;
      }
   }

   private boolean testNestedCondition() {
      boolean result = this.nestedConditionPresent();
      if ((!result || this.ifCondition == null) && this.unlessCondition == null) {
         return result && this.nestedCondition.eval();
      } else {
         throw new BuildException("Nested conditions not permitted in conjunction with if/unless attributes");
      }
   }

   private boolean nestedConditionPresent() {
      return this.nestedCondition != null;
   }

   private static class NestedCondition extends ConditionBase implements Condition {
      private NestedCondition() {
      }

      public boolean eval() {
         if (this.countConditions() != 1) {
            throw new BuildException("A single nested condition is required.");
         } else {
            return ((Condition)((Condition)this.getConditions().nextElement())).eval();
         }
      }

      // $FF: synthetic method
      NestedCondition(Object x0) {
         this();
      }
   }
}
