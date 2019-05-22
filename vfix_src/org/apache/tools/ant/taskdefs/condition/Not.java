package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

public class Not extends ConditionBase implements Condition {
   public boolean eval() throws BuildException {
      if (this.countConditions() > 1) {
         throw new BuildException("You must not nest more than one condition into <not>");
      } else if (this.countConditions() < 1) {
         throw new BuildException("You must nest a condition into <not>");
      } else {
         return !((Condition)this.getConditions().nextElement()).eval();
      }
   }
}
