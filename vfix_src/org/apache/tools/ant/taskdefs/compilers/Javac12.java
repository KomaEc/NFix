package org.apache.tools.ant.taskdefs.compilers;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public class Javac12 extends DefaultCompilerAdapter {
   protected static final String CLASSIC_COMPILER_CLASSNAME = "sun.tools.javac.Main";
   // $FF: synthetic field
   static Class class$java$io$OutputStream;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public boolean execute() throws BuildException {
      this.attributes.log("Using classic compiler", 3);
      Commandline cmd = this.setupJavacCommand(true);
      LogOutputStream logstr = new LogOutputStream(this.attributes, 1);

      boolean var8;
      try {
         Class c = Class.forName("sun.tools.javac.Main");
         Constructor cons = c.getConstructor(class$java$io$OutputStream == null ? (class$java$io$OutputStream = class$("java.io.OutputStream")) : class$java$io$OutputStream, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         Object compiler = cons.newInstance(logstr, "javac");
         Method compile = c.getMethod("compile", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
         Boolean ok = (Boolean)compile.invoke(compiler, cmd.getArguments());
         var8 = ok;
      } catch (ClassNotFoundException var13) {
         throw new BuildException("Cannot use classic compiler , as it is not available. \n A common solution is to set the environment variable JAVA_HOME to your jdk directory.\nIt is currently set to \"" + JavaEnvUtils.getJavaHome() + "\"", this.location);
      } catch (Exception var14) {
         if (var14 instanceof BuildException) {
            throw (BuildException)var14;
         }

         throw new BuildException("Error starting classic compiler: ", var14, this.location);
      } finally {
         FileUtils.close((OutputStream)logstr);
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
