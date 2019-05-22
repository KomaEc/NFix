package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public class RegexpMatcherFactory {
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$util$regexp$RegexpMatcherFactory;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$util$regexp$RegexpMatcher;

   public RegexpMatcher newRegexpMatcher() throws BuildException {
      return this.newRegexpMatcher((Project)null);
   }

   public RegexpMatcher newRegexpMatcher(Project p) throws BuildException {
      String systemDefault = null;
      if (p == null) {
         systemDefault = System.getProperty("ant.regexp.regexpimpl");
      } else {
         systemDefault = p.getProperty("ant.regexp.regexpimpl");
      }

      if (systemDefault != null) {
         return this.createInstance(systemDefault);
      } else {
         Throwable cause = null;

         try {
            this.testAvailability("java.util.regex.Matcher");
            return this.createInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpMatcher");
         } catch (BuildException var7) {
            cause = orCause(cause, var7, JavaEnvUtils.getJavaVersionNumber() < 14);

            try {
               this.testAvailability("org.apache.oro.text.regex.Pattern");
               return this.createInstance("org.apache.tools.ant.util.regexp.JakartaOroMatcher");
            } catch (BuildException var6) {
               cause = orCause(cause, var6, true);

               try {
                  this.testAvailability("org.apache.regexp.RE");
                  return this.createInstance("org.apache.tools.ant.util.regexp.JakartaRegexpMatcher");
               } catch (BuildException var5) {
                  cause = orCause(cause, var5, true);
                  throw new BuildException("No supported regular expression matcher found" + (cause != null ? ": " + cause : ""), cause);
               }
            }
         }
      }
   }

   static Throwable orCause(Throwable deflt, BuildException be, boolean ignoreCnfe) {
      if (deflt != null) {
         return deflt;
      } else {
         Throwable t = be.getException();
         return ignoreCnfe && t instanceof ClassNotFoundException ? null : t;
      }
   }

   protected RegexpMatcher createInstance(String className) throws BuildException {
      return (RegexpMatcher)ClasspathUtils.newInstance(className, (class$org$apache$tools$ant$util$regexp$RegexpMatcherFactory == null ? (class$org$apache$tools$ant$util$regexp$RegexpMatcherFactory = class$("org.apache.tools.ant.util.regexp.RegexpMatcherFactory")) : class$org$apache$tools$ant$util$regexp$RegexpMatcherFactory).getClassLoader(), class$org$apache$tools$ant$util$regexp$RegexpMatcher == null ? (class$org$apache$tools$ant$util$regexp$RegexpMatcher = class$("org.apache.tools.ant.util.regexp.RegexpMatcher")) : class$org$apache$tools$ant$util$regexp$RegexpMatcher);
   }

   protected void testAvailability(String className) throws BuildException {
      try {
         Class.forName(className);
      } catch (Throwable var3) {
         throw new BuildException(var3);
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
