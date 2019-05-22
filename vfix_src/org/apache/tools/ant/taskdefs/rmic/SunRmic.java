package org.apache.tools.ant.taskdefs.rmic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;

public class SunRmic extends DefaultRmicAdapter {
   public static final String RMIC_CLASSNAME = "sun.rmi.rmic.Main";
   public static final String COMPILER_NAME = "sun";
   public static final String RMIC_EXECUTABLE = "rmic";
   public static final String ERROR_NO_RMIC_ON_CLASSPATH = "Cannot use SUN rmic, as it is not available.  A common solution is to set the environment variable JAVA_HOME or CLASSPATH.";
   public static final String ERROR_RMIC_FAILED = "Error starting SUN rmic: ";
   // $FF: synthetic field
   static Class class$java$io$OutputStream;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public boolean execute() throws BuildException {
      this.getRmic().log("Using SUN rmic compiler", 3);
      Commandline cmd = this.setupRmicCommand();
      LogOutputStream logstr = new LogOutputStream(this.getRmic(), 1);

      boolean var8;
      try {
         Class c = Class.forName("sun.rmi.rmic.Main");
         Constructor cons = c.getConstructor(class$java$io$OutputStream == null ? (class$java$io$OutputStream = class$("java.io.OutputStream")) : class$java$io$OutputStream, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         Object rmic = cons.newInstance(logstr, "rmic");
         Method doRmic = c.getMethod("compile", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
         Boolean ok = (Boolean)doRmic.invoke(rmic, cmd.getArguments());
         var8 = ok;
      } catch (ClassNotFoundException var18) {
         throw new BuildException("Cannot use SUN rmic, as it is not available.  A common solution is to set the environment variable JAVA_HOME or CLASSPATH.", this.getRmic().getLocation());
      } catch (Exception var19) {
         if (var19 instanceof BuildException) {
            throw (BuildException)var19;
         }

         throw new BuildException("Error starting SUN rmic: ", var19, this.getRmic().getLocation());
      } finally {
         try {
            logstr.close();
         } catch (IOException var17) {
            throw new BuildException(var17);
         }
      }

      return var8;
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
