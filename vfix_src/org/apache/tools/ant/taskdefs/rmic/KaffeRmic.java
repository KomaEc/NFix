package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecuteJava;
import org.apache.tools.ant.types.Commandline;

public class KaffeRmic extends DefaultRmicAdapter {
   private static final String[] RMIC_CLASSNAMES = new String[]{"gnu.classpath.tools.rmi.rmic.RMIC", "gnu.java.rmi.rmic.RMIC", "kaffe.rmi.rmic.RMIC"};
   public static final String COMPILER_NAME = "kaffe";

   public boolean execute() throws BuildException {
      this.getRmic().log("Using Kaffe rmic", 3);
      Commandline cmd = this.setupRmicCommand();
      Class c = getRmicClass();
      if (c == null) {
         StringBuffer buf = new StringBuffer("Cannot use Kaffe rmic, as it is not available.  None of ");

         for(int i = 0; i < RMIC_CLASSNAMES.length; ++i) {
            if (i != 0) {
               buf.append(", ");
            }

            buf.append(RMIC_CLASSNAMES[i]);
         }

         buf.append(" have been found. A common solution is to set the environment variable JAVA_HOME or CLASSPATH.");
         throw new BuildException(buf.toString(), this.getRmic().getLocation());
      } else {
         cmd.setExecutable(c.getName());
         if (!c.getName().equals(RMIC_CLASSNAMES[RMIC_CLASSNAMES.length - 1])) {
            cmd.createArgument().setValue("-verbose");
            this.getRmic().log(Commandline.describeCommand(cmd));
         }

         ExecuteJava ej = new ExecuteJava();
         ej.setJavaCommand(cmd);
         return ej.fork(this.getRmic()) == 0;
      }
   }

   public static boolean isAvailable() {
      return getRmicClass() != null;
   }

   private static Class getRmicClass() {
      int i = 0;

      while(i < RMIC_CLASSNAMES.length) {
         try {
            return Class.forName(RMIC_CLASSNAMES[i]);
         } catch (ClassNotFoundException var2) {
            ++i;
         }
      }

      return null;
   }
}
