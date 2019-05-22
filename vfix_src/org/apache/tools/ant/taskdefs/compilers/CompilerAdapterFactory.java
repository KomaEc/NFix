package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public final class CompilerAdapterFactory {
   private static final String MODERN_COMPILER = "com.sun.tools.javac.Main";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapter;

   private CompilerAdapterFactory() {
   }

   public static CompilerAdapter getCompiler(String compilerType, Task task) throws BuildException {
      boolean isClassicCompilerSupported = true;
      if (!JavaEnvUtils.isJavaVersion("1.2") && !JavaEnvUtils.isJavaVersion("1.3")) {
         isClassicCompilerSupported = false;
      }

      if (compilerType.equalsIgnoreCase("jikes")) {
         return new Jikes();
      } else if (compilerType.equalsIgnoreCase("extJavac")) {
         return new JavacExternal();
      } else {
         if (compilerType.equalsIgnoreCase("classic") || compilerType.equalsIgnoreCase("javac1.1") || compilerType.equalsIgnoreCase("javac1.2")) {
            if (isClassicCompilerSupported) {
               return new Javac12();
            }

            task.log((String)"This version of java does not support the classic compiler; upgrading to modern", 1);
            compilerType = "modern";
         }

         if (!compilerType.equalsIgnoreCase("modern") && !compilerType.equalsIgnoreCase("javac1.3") && !compilerType.equalsIgnoreCase("javac1.4") && !compilerType.equalsIgnoreCase("javac1.5") && !compilerType.equalsIgnoreCase("javac1.6")) {
            if (!compilerType.equalsIgnoreCase("jvc") && !compilerType.equalsIgnoreCase("microsoft")) {
               if (compilerType.equalsIgnoreCase("kjc")) {
                  return new Kjc();
               } else if (compilerType.equalsIgnoreCase("gcj")) {
                  return new Gcj();
               } else {
                  return (CompilerAdapter)(!compilerType.equalsIgnoreCase("sj") && !compilerType.equalsIgnoreCase("symantec") ? resolveClassName(compilerType) : new Sj());
               }
            } else {
               return new Jvc();
            }
         } else if (doesModernCompilerExist()) {
            return new Javac13();
         } else if (isClassicCompilerSupported) {
            task.log((String)"Modern compiler not found - looking for classic compiler", 1);
            return new Javac12();
         } else {
            throw new BuildException("Unable to find a javac compiler;\ncom.sun.tools.javac.Main is not on the classpath.\nPerhaps JAVA_HOME does not point to the JDK.\nIt is currently set to \"" + JavaEnvUtils.getJavaHome() + "\"");
         }
      }
   }

   private static boolean doesModernCompilerExist() {
      try {
         Class.forName("com.sun.tools.javac.Main");
         return true;
      } catch (ClassNotFoundException var3) {
         try {
            ClassLoader cl = (class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory == null ? (class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory = class$("org.apache.tools.ant.taskdefs.compilers.CompilerAdapterFactory")) : class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory).getClassLoader();
            if (cl != null) {
               cl.loadClass("com.sun.tools.javac.Main");
               return true;
            }
         } catch (ClassNotFoundException var2) {
         }

         return false;
      }
   }

   private static CompilerAdapter resolveClassName(String className) throws BuildException {
      return (CompilerAdapter)ClasspathUtils.newInstance(className, (class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory == null ? (class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory = class$("org.apache.tools.ant.taskdefs.compilers.CompilerAdapterFactory")) : class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapterFactory).getClassLoader(), class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapter == null ? (class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapter = class$("org.apache.tools.ant.taskdefs.compilers.CompilerAdapter")) : class$org$apache$tools$ant$taskdefs$compilers$CompilerAdapter);
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
