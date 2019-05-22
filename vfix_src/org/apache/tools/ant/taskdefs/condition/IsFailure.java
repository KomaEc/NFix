package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.taskdefs.Execute;

public class IsFailure implements Condition {
   private int code;

   public void setCode(int c) {
      this.code = c;
   }

   public int getCode() {
      return this.code;
   }

   public boolean eval() {
      return Execute.isFailure(this.code);
   }
}
