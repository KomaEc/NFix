package org.codehaus.classworlds;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EmbeddedLauncher extends Launcher {
   private static String LAUNCH_METHOD = "execute";
   // $FF: synthetic field
   static Class array$Ljava$lang$String;
   // $FF: synthetic field
   static Class class$org$codehaus$classworlds$ClassWorld;

   public void setAppMain(String mainClassName, String mainRealmName) {
      super.mainClassName = mainClassName;
      super.mainRealmName = mainRealmName;
   }

   public String getMainRealmName() {
      return super.mainRealmName;
   }

   public String getMainClassName() {
      return super.mainClassName;
   }

   public Class getMainClass() throws ClassNotFoundException, NoSuchRealmException {
      return this.getMainRealm().loadClass(this.getMainClassName());
   }

   public ClassRealm getMainRealm() throws NoSuchRealmException {
      return this.getWorld().getRealm(this.getMainRealmName());
   }

   protected Method getEnhancedMainMethod() throws ClassNotFoundException, NoSuchMethodException, NoSuchRealmException {
      Method[] methods = this.getMainClass().getMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method method = methods[i];
         if (LAUNCH_METHOD.equals(method.getName())) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && method.getReturnType() == Void.TYPE) {
               Class[] paramTypes = method.getParameterTypes();
               if (paramTypes.length == 2 && paramTypes[0] == (array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String) && paramTypes[1] == (class$org$codehaus$classworlds$ClassWorld == null ? (class$org$codehaus$classworlds$ClassWorld = class$("org.codehaus.classworlds.ClassWorld")) : class$org$codehaus$classworlds$ClassWorld)) {
                  return method;
               }
            }
         }
      }

      throw new NoSuchMethodException("public void execute(ClassWorld world)");
   }

   protected void launchX() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchRealmException {
      ClassRealm mainRealm = this.getMainRealm();
      Class mainClass = this.getMainClass();
      Method mainMethod = this.getEnhancedMainMethod();
      Thread.currentThread().setContextClassLoader(mainRealm.getClassLoader());
      mainMethod.invoke(mainClass, this.getWorld());
   }

   public void launch() throws Exception {
      String classworldsConf = System.getProperty("classworlds.conf");
      InputStream is = null;
      if (classworldsConf != null) {
         is = new FileInputStream(classworldsConf);
      } else {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         if ("true".equals(System.getProperty("classworlds.bootstrapped"))) {
            is = cl.getResourceAsStream("WORLDS-INF/conf/classworlds.conf");
         } else {
            is = cl.getResourceAsStream("classworlds.conf");
         }
      }

      if (is == null) {
         throw new Exception("classworlds configuration not specified nor found in the classpath");
      } else {
         this.configure((InputStream)is);

         try {
            this.launchX();
         } catch (InvocationTargetException var5) {
            Throwable t = var5.getTargetException();
            if (t instanceof Exception) {
               throw (Exception)t;
            } else if (t instanceof Error) {
               throw (Error)t;
            } else {
               throw var5;
            }
         }
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
