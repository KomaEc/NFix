package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import org.apache.commons.logging.Log;

public class Log4jProxy implements Log, Serializable {
   private static final long serialVersionUID = 1L;
   private static final String PROXY_FQCN = "org.apache.commons.logging.impl.Log4JLogger";
   private Object FATAL;
   private Object ERROR;
   private Object WARN;
   private Object INFO;
   private Object DEBUG;
   private Object TRACE;
   private transient Object logger = null;
   private Method isEnabledFor;
   private Method log;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$impl$Log4jProxy;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   static ClassLoader threadContextClassLoader() {
      PrivilegedAction action = new PrivilegedAction() {
         public Object run() {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            try {
               Class levelClass = loader.loadClass("org.apache.log4j.Level");
               Class loggerClass = loader.loadClass("org.apache.log4j.Logger");
               Class categoryClass = loader.loadClass("org.apache.log4j.Category");
               Class priorityClass = loader.loadClass("org.apache.log4j.Priority");
               ClassLoader testCL = levelClass.getClassLoader();
               if (testCL != loggerClass.getClassLoader() || testCL != categoryClass.getClassLoader() || testCL != loggerClass.getClassLoader() || testCL != priorityClass.getClassLoader()) {
                  loader = (Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy == null ? (Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy = Log4jProxy.class$("org.apache.commons.logging.impl.Log4jProxy")) : Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy).getClassLoader();
               }
            } catch (ClassNotFoundException var7) {
               loader = (Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy == null ? (Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy = Log4jProxy.class$("org.apache.commons.logging.impl.Log4jProxy")) : Log4jProxy.class$org$apache$commons$logging$impl$Log4jProxy).getClassLoader();
            }

            return loader;
         }
      };
      ClassLoader tcl = (ClassLoader)AccessController.doPrivileged(action);
      return tcl;
   }

   Log4jProxy(String name) {
      ClassLoader tcl = threadContextClassLoader();
      Class levelClass = null;
      Class priorityClass = null;
      Class loggerClass = null;

      Class[] sig;
      try {
         levelClass = tcl.loadClass("org.apache.log4j.Level");
         Class[] sig = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
         Method toLevel = levelClass.getMethod("toLevel", sig);
         String[] level = new String[]{"FATAL"};
         this.FATAL = toLevel.invoke((Object)null, level);
         level[0] = "ERROR";
         this.ERROR = toLevel.invoke((Object)null, level);
         level[0] = "WARN";
         this.WARN = toLevel.invoke((Object)null, level);
         level[0] = "INFO";
         this.INFO = toLevel.invoke((Object)null, level);
         level[0] = "DEBUG";
         this.DEBUG = toLevel.invoke((Object)null, level);
         this.TRACE = this.DEBUG;

         try {
            try {
               this.TRACE = levelClass.getDeclaredField("TRACE").get((Object)null);
            } catch (Exception var13) {
               levelClass = tcl.loadClass("org.jboss.logging.XLevel");
               Class[] toLevelSig = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, this.DEBUG.getClass()};
               toLevel = levelClass.getMethod("toLevel", toLevelSig);
               Object[] args = new Object[]{"TRACE", this.DEBUG};
               this.TRACE = toLevel.invoke((Object)null, args);
            }
         } catch (Throwable var14) {
         }

         loggerClass = tcl.loadClass("org.apache.log4j.Logger");
         Method getLogger = loggerClass.getMethod("getLogger", sig);
         Object[] args = new Object[]{name};
         this.logger = getLogger.invoke((Object)null, args);
         priorityClass = tcl.loadClass("org.apache.log4j.Priority");
         sig = new Class[]{priorityClass};
         this.isEnabledFor = loggerClass.getMethod("isEnabledFor", sig);
         Class[] logSig = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, priorityClass, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable};
         this.log = loggerClass.getMethod("log", logSig);
      } catch (Throwable var15) {
         StringBuffer msg = new StringBuffer();
         msg.append("[levelClass, ");
         if (levelClass != null) {
            displayClassInfo(levelClass, msg);
         } else {
            msg.append("null");
         }

         msg.append("]; ");
         msg.append("[priorityClass, ");
         if (priorityClass != null) {
            displayClassInfo(priorityClass, msg);
         } else {
            msg.append("null");
         }

         msg.append("]");
         msg.append("]; ");
         msg.append("[loggerClass, ");
         if (loggerClass != null) {
            displayClassInfo(loggerClass, msg);
            msg.append(", Methods:\n");
            Method[] methods = loggerClass.getMethods();

            for(int n = 0; n < methods.length; ++n) {
               Method m = methods[n];
               msg.append(m);
               msg.append('\n');
               if (m.getName().equals("isEnabledFor")) {
                  sig = m.getParameterTypes();
                  displayClassInfo(sig[0], msg);
               }
            }
         } else {
            msg.append("null");
         }

         msg.append("]");
         throw new UndeclaredThrowableException(var15, msg.toString());
      }
   }

   public boolean isDebugEnabled() {
      return this.isEnabledFor(this.DEBUG);
   }

   public boolean isErrorEnabled() {
      return this.isEnabledFor(this.ERROR);
   }

   public boolean isFatalEnabled() {
      return this.isEnabledFor(this.FATAL);
   }

   public boolean isInfoEnabled() {
      return this.isEnabledFor(this.INFO);
   }

   public boolean isTraceEnabled() {
      return this.isEnabledFor(this.TRACE);
   }

   public boolean isWarnEnabled() {
      return this.isEnabledFor(this.WARN);
   }

   public void trace(Object message) {
      this.log(this.TRACE, message, (Throwable)null);
   }

   public void trace(Object message, Throwable t) {
      this.log(this.TRACE, message, t);
   }

   public void debug(Object message) {
      this.log(this.DEBUG, message, (Throwable)null);
   }

   public void debug(Object message, Throwable t) {
      this.log(this.DEBUG, message, t);
   }

   public void info(Object message) {
      this.log(this.INFO, message, (Throwable)null);
   }

   public void info(Object message, Throwable t) {
      this.log(this.INFO, message, t);
   }

   public void warn(Object message) {
      this.log(this.WARN, message, (Throwable)null);
   }

   public void warn(Object message, Throwable t) {
      this.log(this.WARN, message, t);
   }

   public void error(Object message) {
      this.log(this.ERROR, message, (Throwable)null);
   }

   public void error(Object message, Throwable t) {
      this.log(this.ERROR, message, t);
   }

   public void fatal(Object message) {
      this.log(this.FATAL, message, (Throwable)null);
   }

   public void fatal(Object message, Throwable t) {
      this.log(this.FATAL, message, t);
   }

   private void log(Object level, Object message, Throwable t) {
      Object[] args = new Object[]{"org.apache.commons.logging.impl.Log4JLogger", level, message, t};

      try {
         this.log.invoke(this.logger, args);
      } catch (IllegalAccessException var6) {
         throw new UndeclaredThrowableException(var6);
      } catch (InvocationTargetException var7) {
         throw new UndeclaredThrowableException(var7.getTargetException());
      }
   }

   private boolean isEnabledFor(Object level) {
      Object[] args = new Object[]{level};

      try {
         Boolean flag = (Boolean)this.isEnabledFor.invoke(this.logger, args);
         return flag;
      } catch (IllegalAccessException var4) {
         throw new UndeclaredThrowableException(var4);
      } catch (InvocationTargetException var5) {
         throw new UndeclaredThrowableException(var5.getTargetException());
      }
   }

   public static void displayClassInfo(Class clazz, StringBuffer results) {
      ClassLoader cl = clazz.getClassLoader();
      results.append("\n" + clazz.getName() + "(" + Integer.toHexString(clazz.hashCode()) + ").ClassLoader=" + cl);
      ClassLoader parent = cl;

      int i;
      while(parent != null) {
         results.append("\n.." + parent);
         URL[] urls = getClassLoaderURLs(parent);
         int length = urls != null ? urls.length : 0;

         for(i = 0; i < length; ++i) {
            results.append("\n...." + urls[i]);
         }

         if (parent != null) {
            parent = parent.getParent();
         }
      }

      CodeSource clazzCS = clazz.getProtectionDomain().getCodeSource();
      if (clazzCS != null) {
         results.append("\n++++CodeSource: " + clazzCS);
      } else {
         results.append("\n++++Null CodeSource");
      }

      results.append("\nImplemented Interfaces:");
      Class[] ifaces = clazz.getInterfaces();

      for(i = 0; i < ifaces.length; ++i) {
         Class iface = ifaces[i];
         results.append("\n++" + iface + "(" + Integer.toHexString(iface.hashCode()) + ")");
         ClassLoader loader = ifaces[i].getClassLoader();
         results.append("\n++++ClassLoader: " + loader);
         ProtectionDomain pd = ifaces[i].getProtectionDomain();
         CodeSource cs = pd.getCodeSource();
         if (cs != null) {
            results.append("\n++++CodeSource: " + cs);
         } else {
            results.append("\n++++Null CodeSource");
         }
      }

   }

   public static URL[] getClassLoaderURLs(ClassLoader cl) {
      URL[] urls = new URL[0];

      try {
         Class returnType = urls.getClass();
         Class[] parameterTypes = new Class[0];
         Class clClass = cl.getClass();
         Method getURLs = clClass.getMethod("getURLs", parameterTypes);
         if (returnType.isAssignableFrom(getURLs.getReturnType())) {
            Object[] args = new Object[0];
            urls = (URL[])((URL[])getURLs.invoke(cl, args));
         }

         if (urls == null || urls.length == 0) {
            Method getCp = clClass.getMethod("getClasspath", parameterTypes);
            if (returnType.isAssignableFrom(getCp.getReturnType())) {
               Object[] args = new Object[0];
               urls = (URL[])((URL[])getCp.invoke(cl, args));
            }
         }
      } catch (Exception var8) {
      }

      return urls;
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
