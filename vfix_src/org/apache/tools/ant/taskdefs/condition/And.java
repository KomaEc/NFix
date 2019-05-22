package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import org.apache.tools.ant.BuildException;

public class And extends ConditionBase implements Condition {
   public boolean eval() throws BuildException {
      Enumeration e = this.getConditions();

      Condition c;
      do {
         if (!e.hasMoreElements()) {
            return true;
         }

         c = (Condition)e.nextElement();
      } while(c.eval());

      return false;
   }
}
