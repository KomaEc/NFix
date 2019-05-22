package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

public class Contains implements Condition {
   private String string;
   private String subString;
   private boolean caseSensitive = true;

   public void setString(String string) {
      this.string = string;
   }

   public void setSubstring(String subString) {
      this.subString = subString;
   }

   public void setCasesensitive(boolean b) {
      this.caseSensitive = b;
   }

   public boolean eval() throws BuildException {
      if (this.string != null && this.subString != null) {
         return this.caseSensitive ? this.string.indexOf(this.subString) > -1 : this.string.toLowerCase().indexOf(this.subString.toLowerCase()) > -1;
      } else {
         throw new BuildException("both string and substring are required in contains");
      }
   }
}
