package org.codehaus.classworlds.uberjar.boot;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Bootstrapper {
   public static final String LAUNCHER_CLASS_NAME = "org.codehaus.classworlds.Launcher";
   private String[] args;
   private InitialClassLoader classLoader;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public static void main(String[] args) throws Exception {
      System.setProperty("java.protocol.handler.pkgs", "org.codehaus.classworlds.uberjar.protocol");
      Bootstrapper bootstrapper = new Bootstrapper(args);
      bootstrapper.bootstrap();
   }

   public Bootstrapper(String[] args) throws Exception {
      this.args = args;
      this.classLoader = new InitialClassLoader();
   }

   protected ClassLoader getInitialClassLoader() {
      return this.classLoader;
   }

   public void bootstrap() throws Exception {
      ClassLoader cl = this.getInitialClassLoader();
      Class launcherClass = cl.loadClass("org.codehaus.classworlds.Launcher");
      Method[] methods = launcherClass.getMethods();
      Method mainMethod = null;

      for(int i = 0; i < methods.length; ++i) {
         if ("main".equals(methods[i].getName())) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && methods[i].getReturnType() == Void.TYPE) {
               Class[] paramTypes = methods[i].getParameterTypes();
               if (paramTypes.length == 1 && paramTypes[0] == (array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String)) {
                  mainMethod = methods[i];
                  break;
               }
            }
         }
      }

      if (mainMethod == null) {
         throw new NoSuchMethodException("org.codehaus.classworlds.Launcher::main(String[] args)");
      } else {
         System.setProperty("classworlds.bootstrapped", "true");
         mainMethod.invoke(launcherClass, this.args);
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
