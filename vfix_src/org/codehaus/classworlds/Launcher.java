package org.codehaus.classworlds;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;

public class Launcher {
   protected static final String CLASSWORLDS_CONF = "classworlds.conf";
   protected static final String UBERJAR_CONF_DIR = "WORLDS-INF/conf/";
   protected ClassLoader systemClassLoader;
   protected String mainClassName;
   protected String mainRealmName;
   protected ClassWorld world;
   private int exitCode = 0;
   // $FF: synthetic field
   static Class class$org$codehaus$classworlds$ClassWorld;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public void setSystemClassLoader(ClassLoader loader) {
      this.systemClassLoader = loader;
   }

   public ClassLoader getSystemClassLoader() {
      return this.systemClassLoader;
   }

   public int getExitCode() {
      return this.exitCode;
   }

   public void setAppMain(String mainClassName, String mainRealmName) {
      this.mainClassName = mainClassName;
      this.mainRealmName = mainRealmName;
   }

   public String getMainRealmName() {
      return this.mainRealmName;
   }

   public String getMainClassName() {
      return this.mainClassName;
   }

   public void setWorld(ClassWorld world) {
      this.world = world;
   }

   public ClassWorld getWorld() {
      return this.world;
   }

   public void configure(InputStream is) throws IOException, MalformedURLException, ConfigurationException, DuplicateRealmException, NoSuchRealmException {
      Configurator configurator = new Configurator(this);
      configurator.configure(is);
   }

   public Class getMainClass() throws ClassNotFoundException, NoSuchRealmException {
      return this.getMainRealm().loadClass(this.getMainClassName());
   }

   public ClassRealm getMainRealm() throws NoSuchRealmException {
      return this.getWorld().getRealm(this.getMainRealmName());
   }

   protected Method getEnhancedMainMethod() throws ClassNotFoundException, NoSuchMethodException, NoSuchRealmException {
      Method[] methods = this.getMainClass().getMethods();
      Class cwClass = this.getMainRealm().loadClass((class$org$codehaus$classworlds$ClassWorld == null ? (class$org$codehaus$classworlds$ClassWorld = class$("org.codehaus.classworlds.ClassWorld")) : class$org$codehaus$classworlds$ClassWorld).getName());
      Method m = this.getMainClass().getMethod("main", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String, cwClass);
      int modifiers = m.getModifiers();
      if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || m.getReturnType() != Integer.TYPE && m.getReturnType() != Void.TYPE) {
         throw new NoSuchMethodException("public static void main(String[] args, ClassWorld world)");
      } else {
         return m;
      }
   }

   protected Method getMainMethod() throws ClassNotFoundException, NoSuchMethodException, NoSuchRealmException {
      Method m = this.getMainClass().getMethod("main", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
      int modifiers = m.getModifiers();
      if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || m.getReturnType() != Integer.TYPE && m.getReturnType() != Void.TYPE) {
         throw new NoSuchMethodException("public static void main(String[] args) in " + this.getMainClass());
      } else {
         return m;
      }
   }

   public void launch(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchRealmException {
      try {
         this.launchEnhanced(args);
      } catch (NoSuchMethodException var3) {
         this.launchStandard(args);
      }
   }

   protected void launchEnhanced(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchRealmException {
      ClassRealm mainRealm = this.getMainRealm();
      Class mainClass = this.getMainClass();
      Method mainMethod = this.getEnhancedMainMethod();
      ClassLoader cl = mainRealm.getClassLoader();
      Thread.currentThread().setContextClassLoader(cl);
      Object ret = mainMethod.invoke(mainClass, args, this.getWorld());
      if (ret instanceof Integer) {
         this.exitCode = (Integer)ret;
      }

   }

   protected void launchStandard(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchRealmException {
      ClassRealm mainRealm = this.getMainRealm();
      Class mainClass = this.getMainClass();
      Method mainMethod = this.getMainMethod();
      Thread.currentThread().setContextClassLoader(mainRealm.getClassLoader());
      Object ret = mainMethod.invoke(mainClass, args);
      if (ret instanceof Integer) {
         this.exitCode = (Integer)ret;
      }

   }

   public static void main(String[] args) {
      try {
         int exitCode = mainWithExitCode(args);
         System.exit(exitCode);
      } catch (Exception var2) {
         var2.printStackTrace();
         System.exit(100);
      }

   }

   public static int mainWithExitCode(String[] args) throws Exception {
      String classworldsConf = System.getProperty("classworlds.conf");
      InputStream is = null;
      Launcher launcher = new Launcher();
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      launcher.setSystemClassLoader(cl);
      if (classworldsConf != null) {
         is = new FileInputStream(classworldsConf);
      } else if ("true".equals(System.getProperty("classworlds.bootstrapped"))) {
         is = cl.getResourceAsStream("WORLDS-INF/conf/classworlds.conf");
      } else {
         is = cl.getResourceAsStream("classworlds.conf");
      }

      if (is == null) {
         throw new Exception("classworlds configuration not specified nor found in the classpath");
      } else {
         launcher.configure((InputStream)is);

         try {
            launcher.launch(args);
         } catch (InvocationTargetException var9) {
            ClassRealm realm = launcher.getWorld().getRealm(launcher.getMainRealmName());
            URL[] constituents = realm.getConstituents();
            System.out.println("---------------------------------------------------");

            for(int i = 0; i < constituents.length; ++i) {
               System.out.println("constituent[" + i + "]: " + constituents[i]);
            }

            System.out.println("---------------------------------------------------");
            Throwable t = var9.getTargetException();
            if (t instanceof Exception) {
               throw (Exception)t;
            }

            if (t instanceof Error) {
               throw (Error)t;
            }

            throw var9;
         }

         return launcher.getExitCode();
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
