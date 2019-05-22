package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.DeweyDecimal;

public class AntVersion implements Condition {
   private String atLeast = null;
   private String exactly = null;

   public boolean eval() throws BuildException {
      this.validate();
      DeweyDecimal actual = this.getVersion();
      if (null != this.atLeast) {
         return actual.isGreaterThanOrEqual(new DeweyDecimal(this.atLeast));
      } else {
         return null != this.exactly ? actual.isEqual(new DeweyDecimal(this.exactly)) : false;
      }
   }

   private void validate() throws BuildException {
      if (this.atLeast != null && this.exactly != null) {
         throw new BuildException("Only one of atleast or exactly may be set.");
      } else if (null == this.atLeast && null == this.exactly) {
         throw new BuildException("One of atleast or exactly must be set.");
      } else {
         try {
            if (this.atLeast != null) {
               new DeweyDecimal(this.atLeast);
            } else {
               new DeweyDecimal(this.exactly);
            }

         } catch (NumberFormatException var2) {
            throw new BuildException("The argument is not a Dewey Decimal eg 1.1.0");
         }
      }
   }

   private DeweyDecimal getVersion() {
      Project p = new Project();
      p.init();
      char[] versionString = p.getProperty("ant.version").toCharArray();
      StringBuffer sb = new StringBuffer();
      boolean foundFirstDigit = false;

      for(int i = 0; i < versionString.length; ++i) {
         if (Character.isDigit(versionString[i])) {
            sb.append(versionString[i]);
            foundFirstDigit = true;
         }

         if (versionString[i] == '.' && foundFirstDigit) {
            sb.append(versionString[i]);
         }

         if (Character.isLetter(versionString[i]) && foundFirstDigit) {
            break;
         }
      }

      return new DeweyDecimal(sb.toString());
   }

   public String getAtLeast() {
      return this.atLeast;
   }

   public void setAtLeast(String atLeast) {
      this.atLeast = atLeast;
   }

   public String getExactly() {
      return this.exactly;
   }

   public void setExactly(String exactly) {
      this.exactly = exactly;
   }
}
