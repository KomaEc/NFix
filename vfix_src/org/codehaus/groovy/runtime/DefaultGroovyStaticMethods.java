package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import org.codehaus.groovy.reflection.ReflectionUtils;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class DefaultGroovyStaticMethods {
   public static Thread start(Thread self, Closure closure) {
      return createThread((String)null, false, closure);
   }

   public static Thread start(Thread self, String name, Closure closure) {
      return createThread(name, false, closure);
   }

   public static Thread startDaemon(Thread self, Closure closure) {
      return createThread((String)null, true, closure);
   }

   public static Thread startDaemon(Thread self, String name, Closure closure) {
      return createThread(name, true, closure);
   }

   private static Thread createThread(String name, boolean daemon, Closure closure) {
      Thread thread = name != null ? new Thread(closure, name) : new Thread(closure);
      if (daemon) {
         thread.setDaemon(true);
      }

      thread.start();
      return thread;
   }

   public static Matcher getLastMatcher(Matcher self) {
      return RegexSupport.getLastMatcher();
   }

   private static void sleepImpl(long millis, Closure closure) {
      long start = System.currentTimeMillis();
      long rest = millis;

      while(rest > 0L) {
         try {
            Thread.sleep(rest);
            rest = 0L;
         } catch (InterruptedException var10) {
            if (closure != null && DefaultTypeTransformation.castToBoolean(closure.call((Object)var10))) {
               return;
            }

            long current = System.currentTimeMillis();
            rest = millis + start - current;
         }
      }

   }

   public static void sleep(Object self, long milliseconds) {
      sleepImpl(milliseconds, (Closure)null);
   }

   public static void sleep(Object self, long milliseconds, Closure onInterrupt) {
      sleepImpl(milliseconds, onInterrupt);
   }

   public static Date parse(Date self, String format, String input) throws ParseException {
      return (new SimpleDateFormat(format)).parse(input);
   }

   public static ResourceBundle getBundle(ResourceBundle self, String bundleName) {
      return getBundle(self, bundleName, Locale.getDefault());
   }

   public static ResourceBundle getBundle(ResourceBundle self, String bundleName, Locale locale) {
      Class c = ReflectionUtils.getCallingClass();
      ClassLoader targetCL = c != null ? c.getClassLoader() : null;
      if (targetCL == null) {
         targetCL = ClassLoader.getSystemClassLoader();
      }

      return ResourceBundle.getBundle(bundleName, locale, targetCL);
   }
}
