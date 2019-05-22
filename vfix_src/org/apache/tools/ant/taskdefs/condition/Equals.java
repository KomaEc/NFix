package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

public class Equals implements Condition {
   private String arg1;
   private String arg2;
   private boolean trim = false;
   private boolean caseSensitive = true;

   public void setArg1(String a1) {
      this.arg1 = a1;
   }

   public void setArg2(String a2) {
      this.arg2 = a2;
   }

   public void setTrim(boolean b) {
      this.trim = b;
   }

   public void setCasesensitive(boolean b) {
      this.caseSensitive = b;
   }

   public boolean eval() throws BuildException {
      if (this.arg1 != null && this.arg2 != null) {
         if (this.trim) {
            this.arg1 = this.arg1.trim();
            this.arg2 = this.arg2.trim();
         }

         return this.caseSensitive ? this.arg1.equals(this.arg2) : this.arg1.equalsIgnoreCase(this.arg2);
      } else {
         throw new BuildException("both arg1 and arg2 are required in equals");
      }
   }
}
