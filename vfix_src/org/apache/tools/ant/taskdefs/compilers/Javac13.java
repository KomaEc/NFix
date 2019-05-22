package org.apache.tools.ant.taskdefs.compilers;

import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

public class Javac13 extends DefaultCompilerAdapter {
   private static final int MODERN_COMPILER_SUCCESS = 0;

   public boolean execute() throws BuildException {
      this.attributes.log("Using modern compiler", 3);
      Commandline cmd = this.setupModernJavacCommand();

      try {
         Class c = Class.forName("com.sun.tools.javac.Main");
         Object compiler = c.newInstance();
         Method compile = c.getMethod("compile", (new String[0]).getClass());
         int result = (Integer)compile.invoke(compiler, cmd.getArguments());
         return result == 0;
      } catch (Exception var6) {
         if (var6 instanceof BuildException) {
            throw (BuildException)var6;
         } else {
            throw new BuildException("Error starting modern compiler", var6, this.location);
         }
      }
   }
}
