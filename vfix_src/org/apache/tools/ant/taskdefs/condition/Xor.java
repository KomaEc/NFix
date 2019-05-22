package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import org.apache.tools.ant.BuildException;

public class Xor extends ConditionBase implements Condition {
   public boolean eval() throws BuildException {
      Enumeration e = this.getConditions();

      boolean state;
      Condition c;
      for(state = false; e.hasMoreElements(); state ^= c.eval()) {
         c = (Condition)e.nextElement();
      }

      return state;
   }
}
