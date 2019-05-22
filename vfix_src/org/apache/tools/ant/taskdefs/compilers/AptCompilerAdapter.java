package org.apache.tools.ant.taskdefs.compilers;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Apt;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

public class AptCompilerAdapter extends DefaultCompilerAdapter {
   private static final int APT_COMPILER_SUCCESS = 0;
   public static final String APT_ENTRY_POINT = "com.sun.tools.apt.Main";
   public static final String APT_METHOD_NAME = "process";

   protected Apt getApt() {
      return (Apt)this.getJavac();
   }

   static void setAptCommandlineSwitches(Apt apt, Commandline cmd) {
      if (!apt.isCompile()) {
         cmd.createArgument().setValue("-nocompile");
      }

      String factory = apt.getFactory();
      if (factory != null) {
         cmd.createArgument().setValue("-factory");
         cmd.createArgument().setValue(factory);
      }

      Path factoryPath = apt.getFactoryPath();
      if (factoryPath != null) {
         cmd.createArgument().setValue("-factorypath");
         cmd.createArgument().setPath(factoryPath);
      }

      File preprocessDir = apt.getPreprocessDir();
      if (preprocessDir != null) {
         cmd.createArgument().setValue("-s");
         cmd.createArgument().setFile(preprocessDir);
      }

      Vector options = apt.getOptions();
      Enumeration elements = options.elements();

      for(StringBuffer arg = null; elements.hasMoreElements(); cmd.createArgument().setValue(arg.toString())) {
         Apt.Option opt = (Apt.Option)elements.nextElement();
         arg = new StringBuffer();
         arg.append("-A").append(opt.getName());
         if (opt.getValue() != null) {
            arg.append("=").append(opt.getValue());
         }
      }

   }

   protected void setAptCommandlineSwitches(Commandline cmd) {
      Apt apt = this.getApt();
      setAptCommandlineSwitches(apt, cmd);
   }

   public boolean execute() throws BuildException {
      this.attributes.log("Using apt compiler", 3);
      Commandline cmd = this.setupModernJavacCommand();
      this.setAptCommandlineSwitches(cmd);

      try {
         Class c = Class.forName("com.sun.tools.apt.Main");
         Object compiler = c.newInstance();
         Method compile = c.getMethod("process", (new String[0]).getClass());
         int result = (Integer)compile.invoke(compiler, cmd.getArguments());
         return result == 0;
      } catch (BuildException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new BuildException("Error starting apt compiler", var7, this.location);
      }
   }
}
