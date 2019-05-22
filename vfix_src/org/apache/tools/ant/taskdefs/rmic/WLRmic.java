package org.apache.tools.ant.taskdefs.rmic;

import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

public class WLRmic extends DefaultRmicAdapter {
   public static final String WLRMIC_CLASSNAME = "weblogic.rmic";
   public static final String COMPILER_NAME = "weblogic";
   public static final String ERROR_NO_WLRMIC_ON_CLASSPATH = "Cannot use WebLogic rmic, as it is not available.  A common solution is to set the environment variable CLASSPATH.";
   public static final String ERROR_WLRMIC_FAILED = "Error starting WebLogic rmic: ";
   public static final String WL_RMI_STUB_SUFFIX = "_WLStub";
   public static final String WL_RMI_SKEL_SUFFIX = "_WLSkel";
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public boolean execute() throws BuildException {
      this.getRmic().log("Using WebLogic rmic", 3);
      Commandline cmd = this.setupRmicCommand(new String[]{"-noexit"});
      AntClassLoader loader = null;

      boolean var5;
      try {
         Class c = null;
         if (this.getRmic().getClasspath() == null) {
            c = Class.forName("weblogic.rmic");
         } else {
            loader = this.getRmic().getProject().createClassLoader(this.getRmic().getClasspath());
            c = Class.forName("weblogic.rmic", true, loader);
         }

         Method doRmic = c.getMethod("main", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
         doRmic.invoke((Object)null, cmd.getArguments());
         var5 = true;
      } catch (ClassNotFoundException var10) {
         throw new BuildException("Cannot use WebLogic rmic, as it is not available.  A common solution is to set the environment variable CLASSPATH.", this.getRmic().getLocation());
      } catch (Exception var11) {
         if (var11 instanceof BuildException) {
            throw (BuildException)var11;
         }

         throw new BuildException("Error starting WebLogic rmic: ", var11, this.getRmic().getLocation());
      } finally {
         if (loader != null) {
            loader.cleanup();
         }

      }

      return var5;
   }

   public String getStubClassSuffix() {
      return "_WLStub";
   }

   public String getSkelClassSuffix() {
      return "_WLSkel";
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
