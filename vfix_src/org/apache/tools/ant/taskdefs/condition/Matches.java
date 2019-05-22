package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.Regexp;

public class Matches extends ProjectComponent implements Condition {
   private String string;
   private boolean caseSensitive = true;
   private boolean multiLine = false;
   private boolean singleLine = false;
   private RegularExpression regularExpression;

   public void setString(String string) {
      this.string = string;
   }

   public void setPattern(String pattern) {
      if (this.regularExpression != null) {
         throw new BuildException("Only one regular expression is allowed.");
      } else {
         this.regularExpression = new RegularExpression();
         this.regularExpression.setPattern(pattern);
      }
   }

   public void addRegexp(RegularExpression regularExpression) {
      if (this.regularExpression != null) {
         throw new BuildException("Only one regular expression is allowed.");
      } else {
         this.regularExpression = regularExpression;
      }
   }

   public void setCasesensitive(boolean b) {
      this.caseSensitive = b;
   }

   public void setMultiline(boolean b) {
      this.multiLine = b;
   }

   public void setSingleLine(boolean b) {
      this.singleLine = b;
   }

   public boolean eval() throws BuildException {
      if (this.string == null) {
         throw new BuildException("Parameter string is required in matches.");
      } else if (this.regularExpression == null) {
         throw new BuildException("Missing pattern in matches.");
      } else {
         int options = 0;
         if (!this.caseSensitive) {
            options |= 256;
         }

         if (this.multiLine) {
            options |= 4096;
         }

         if (this.singleLine) {
            options |= 65536;
         }

         Regexp regexp = this.regularExpression.getRegexp(this.getProject());
         return regexp.matches(this.string, options);
      }
   }
}
