package org.codehaus.groovy.tools.javac;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class JavacJavaCompiler implements JavaCompiler {
   private CompilerConfiguration config;

   public JavacJavaCompiler(CompilerConfiguration config) {
      this.config = config;
   }

   public void compile(List<String> files, CompilationUnit cu) {
      String[] javacParameters = this.makeParameters(files, cu.getClassLoader());
      StringWriter javacOutput = null;
      int javacReturnValue = 0;

      try {
         Class javac = this.findJavac(cu);
         Method method = null;

         try {
            method = javac.getMethod("compile", String[].class, PrintWriter.class);
            javacOutput = new StringWriter();
            PrintWriter writer = new PrintWriter(javacOutput);
            Object ret = method.invoke((Object)null, javacParameters, writer);
            javacReturnValue = (Integer)ret;
         } catch (NoSuchMethodException var10) {
         }

         if (method == null) {
            method = javac.getMethod("compile", String[].class);
            Object ret = method.invoke((Object)null, javacParameters);
            javacReturnValue = (Integer)ret;
         }

         cu.getConfiguration().getOutput();
      } catch (InvocationTargetException var11) {
         cu.getErrorCollector().addFatalError(new ExceptionMessage((Exception)var11.getCause(), true, cu));
      } catch (Exception var12) {
         cu.getErrorCollector().addFatalError(new ExceptionMessage(var12, true, cu));
      }

      if (javacReturnValue != 0) {
         switch(javacReturnValue) {
         case 1:
            this.addJavacError("Compile error during compilation with javac.", cu, javacOutput);
            break;
         case 2:
            this.addJavacError("Invalid commandline usage for javac.", cu, javacOutput);
            break;
         case 3:
            this.addJavacError("System error during compilation with javac.", cu, javacOutput);
            break;
         case 4:
            this.addJavacError("Abnormal termination of javac.", cu, javacOutput);
            break;
         default:
            this.addJavacError("unexpected return value by javac.", cu, javacOutput);
         }
      } else {
         System.out.print(javacOutput);
      }

   }

   private void addJavacError(String header, CompilationUnit cu, StringWriter msg) {
      if (msg != null) {
         header = header + "\n" + msg.getBuffer().toString();
      } else {
         header = header + "\nThis javac version does not support compile(String[],PrintWriter), " + "so no further details of the error are available. The message error text " + "should be found on System.err.\n";
      }

      cu.getErrorCollector().addFatalError(new SimpleMessage(header, cu));
   }

   private String[] makeParameters(List<String> files, GroovyClassLoader parentClassLoader) {
      Map options = this.config.getJointCompilationOptions();
      LinkedList<String> paras = new LinkedList();
      File target = this.config.getTargetDirectory();
      if (target == null) {
         target = new File(".");
      }

      paras.add("-d");
      paras.add(target.getAbsolutePath());
      paras.add("-sourcepath");
      paras.add(((File)options.get("stubDir")).getAbsolutePath());
      String[] flags = (String[])((String[])options.get("flags"));
      int i;
      String name;
      if (flags != null) {
         String[] arr$ = flags;
         int len$ = flags.length;

         for(i = 0; i < len$; ++i) {
            name = arr$[i];
            paras.add('-' + name);
         }
      }

      boolean hadClasspath = false;
      String[] namedValues = (String[])((String[])options.get("namedValues"));
      if (namedValues != null) {
         for(i = 0; i < namedValues.length; i += 2) {
            name = namedValues[i];
            if (name.equals("classpath")) {
               hadClasspath = true;
            }

            paras.add('-' + name);
            paras.add(namedValues[i + 1]);
         }
      }

      if (!hadClasspath) {
         StringBuffer resultPath = new StringBuffer(DefaultGroovyMethods.join((Collection)this.config.getClasspath(), File.pathSeparator));

         for(Object cl = parentClassLoader; cl != null; cl = ((ClassLoader)cl).getParent()) {
            if (cl instanceof URLClassLoader) {
               URL[] arr$ = ((URLClassLoader)cl).getURLs();
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  URL u = arr$[i$];

                  try {
                     resultPath.append(File.pathSeparator);
                     resultPath.append((new File(u.toURI())).getPath());
                  } catch (URISyntaxException var16) {
                  }
               }
            }
         }

         paras.add("-classpath");
         paras.add(resultPath.toString());
      }

      paras.addAll(files);
      return (String[])paras.toArray(new String[paras.size()]);
   }

   private Class findJavac(CompilationUnit cu) throws ClassNotFoundException {
      String main = "com.sun.tools.javac.Main";

      try {
         return Class.forName(main);
      } catch (ClassNotFoundException var9) {
         try {
            ClassLoader cl = this.getClass().getClassLoader();
            return cl.loadClass(main);
         } catch (ClassNotFoundException var8) {
            try {
               return ClassLoader.getSystemClassLoader().loadClass(main);
            } catch (ClassNotFoundException var7) {
               try {
                  return cu.getClassLoader().getParent().loadClass(main);
               } catch (ClassNotFoundException var6) {
                  String javaHome = System.getProperty("java.home");
                  if (javaHome.toLowerCase(Locale.US).endsWith("jre")) {
                     javaHome = javaHome.substring(0, javaHome.length() - 4);
                  }

                  File toolsJar = new File(javaHome + "/lib/tools.jar");
                  if (toolsJar.exists()) {
                     GroovyClassLoader loader = cu.getClassLoader();
                     loader.addClasspath(toolsJar.getAbsolutePath());
                     return loader.loadClass(main);
                  } else {
                     throw new ClassNotFoundException("unable to locate the java compiler com.sun.tools.javac.Main, please change your classloader settings");
                  }
               }
            }
         }
      }
   }
}
