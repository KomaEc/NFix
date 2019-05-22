package org.codehaus.groovy.tools;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GroovyStarter {
   static void printUsage() {
      System.out.println("possible programs are 'groovyc','groovy','console', and 'groovysh'");
      System.exit(1);
   }

   public static void rootLoader(String[] args) {
      String conf = System.getProperty("groovy.starter.conf", (String)null);
      LoaderConfiguration lc = new LoaderConfiguration();
      boolean hadMain = false;
      boolean hadConf = false;
      boolean hadCP = false;
      int argsOffset = 0;

      while(args.length - argsOffset > 0 && (!hadMain || !hadConf || !hadCP)) {
         if (args[argsOffset].equals("--classpath")) {
            if (hadCP) {
               break;
            }

            if (args.length == argsOffset + 1) {
               exit("classpath parameter needs argument");
            }

            lc.addClassPath(args[argsOffset + 1]);
            argsOffset += 2;
            hadCP = true;
         } else if (args[argsOffset].equals("--main")) {
            if (hadMain) {
               break;
            }

            if (args.length == argsOffset + 1) {
               exit("main parameter needs argument");
            }

            lc.setMainClass(args[argsOffset + 1]);
            argsOffset += 2;
            hadMain = true;
         } else {
            if (!args[argsOffset].equals("--conf") || hadConf) {
               break;
            }

            if (args.length == argsOffset + 1) {
               exit("conf parameter needs argument");
            }

            conf = args[argsOffset + 1];
            argsOffset += 2;
            hadConf = true;
         }
      }

      String confOverride = System.getProperty("groovy.starter.conf.override", (String)null);
      if (confOverride != null) {
         conf = confOverride;
      }

      if (lc.getMainClass() == null && conf == null) {
         exit("no configuration file or main class specified");
      }

      String[] newArgs = new String[args.length - argsOffset];

      for(int i = 0; i < newArgs.length; ++i) {
         newArgs[i] = args[i + argsOffset];
      }

      if (conf != null) {
         try {
            lc.configure(new FileInputStream(conf));
         } catch (Exception var18) {
            System.err.println("exception while configuring main class loader:");
            exit(var18);
         }
      }

      ClassLoader loader = new RootLoader(lc);
      Method m = null;

      try {
         Class c = loader.loadClass(lc.getMainClass());
         m = c.getMethod("main", String[].class);
      } catch (ClassNotFoundException var15) {
         exit((Exception)var15);
      } catch (SecurityException var16) {
         exit((Exception)var16);
      } catch (NoSuchMethodException var17) {
         exit((Exception)var17);
      }

      try {
         m.invoke((Object)null, newArgs);
      } catch (IllegalArgumentException var12) {
         exit((Exception)var12);
      } catch (IllegalAccessException var13) {
         exit((Exception)var13);
      } catch (InvocationTargetException var14) {
         exit((Exception)var14);
      }

   }

   private static void exit(Exception e) {
      e.printStackTrace();
      System.exit(1);
   }

   private static void exit(String msg) {
      System.err.println(msg);
      System.exit(1);
   }

   public static void main(String[] args) {
      try {
         rootLoader(args);
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }
}
