package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

public class RegexpFactory extends RegexpMatcherFactory {
   public Regexp newRegexp() throws BuildException {
      return this.newRegexp((Project)null);
   }

   public Regexp newRegexp(Project p) throws BuildException {
      String systemDefault = null;
      if (p == null) {
         systemDefault = System.getProperty("ant.regexp.regexpimpl");
      } else {
         systemDefault = p.getProperty("ant.regexp.regexpimpl");
      }

      if (systemDefault != null) {
         return this.createRegexpInstance(systemDefault);
      } else {
         Throwable cause = null;

         try {
            this.testAvailability("java.util.regex.Matcher");
            return this.createRegexpInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpRegexp");
         } catch (BuildException var7) {
            cause = orCause(cause, var7, JavaEnvUtils.getJavaVersionNumber() < 14);

            try {
               this.testAvailability("org.apache.oro.text.regex.Pattern");
               return this.createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaOroRegexp");
            } catch (BuildException var6) {
               cause = orCause(cause, var6, true);

               try {
                  this.testAvailability("org.apache.regexp.RE");
                  return this.createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaRegexpRegexp");
               } catch (BuildException var5) {
                  cause = orCause(cause, var5, true);
                  throw new BuildException("No supported regular expression matcher found" + (cause != null ? ": " + cause : ""), cause);
               }
            }
         }
      }
   }

   protected Regexp createRegexpInstance(String classname) throws BuildException {
      RegexpMatcher m = this.createInstance(classname);
      if (m instanceof Regexp) {
         return (Regexp)m;
      } else {
         throw new BuildException(classname + " doesn't implement the Regexp interface");
      }
   }
}
