package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class StackTraceUtils {
   public static final String STACK_LOG_NAME = "StackTrace";
   private static final Logger STACK_LOG;
   private static final String[] GROOVY_PACKAGES;
   private static List<Closure> tests;

   public static void addClassTest(Closure test) {
      tests.add(test);
   }

   public static Throwable sanitize(Throwable t) {
      if (!Boolean.getBoolean("groovy.full.stacktrace")) {
         StackTraceElement[] trace = t.getStackTrace();
         List<StackTraceElement> newTrace = new ArrayList();
         StackTraceElement[] clean = trace;
         int len$ = trace.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            StackTraceElement stackTraceElement = clean[i$];
            if (isApplicationClass(stackTraceElement.getClassName())) {
               newTrace.add(stackTraceElement);
            }
         }

         STACK_LOG.log(Level.WARNING, "Sanitizing stacktrace:", t);
         clean = new StackTraceElement[newTrace.size()];
         newTrace.toArray(clean);
         t.setStackTrace(clean);
      }

      return t;
   }

   public static void printSanitizedStackTrace(Throwable t, PrintWriter p) {
      t = sanitize(t);
      StackTraceElement[] trace = t.getStackTrace();
      StackTraceElement[] arr$ = trace;
      int len$ = trace.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         StackTraceElement stackTraceElement = arr$[i$];
         p.println("at " + stackTraceElement.getClassName() + "(" + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber() + ")");
      }

   }

   public static void printSanitizedStackTrace(Throwable t) {
      printSanitizedStackTrace(t, new PrintWriter(System.err));
   }

   public static boolean isApplicationClass(String className) {
      Iterator i$ = tests.iterator();

      Object result;
      do {
         if (!i$.hasNext()) {
            String[] arr$ = GROOVY_PACKAGES;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String groovyPackage = arr$[i$];
               if (className.startsWith(groovyPackage)) {
                  return false;
               }
            }

            return true;
         }

         Closure test = (Closure)i$.next();
         result = test.call((Object)className);
      } while(result == null);

      return DefaultTypeTransformation.castToBoolean(result);
   }

   public static Throwable extractRootCause(Throwable t) {
      Throwable result;
      for(result = t; result.getCause() != null; result = result.getCause()) {
      }

      return result;
   }

   public static Throwable sanitizeRootCause(Throwable t) {
      return sanitize(extractRootCause(t));
   }

   public static Throwable deepSanitize(Throwable t) {
      for(Throwable current = t; current.getCause() != null; current = sanitize(current.getCause())) {
      }

      return sanitize(t);
   }

   static {
      Enumeration existingLogs = LogManager.getLogManager().getLoggerNames();

      while(true) {
         if (existingLogs.hasMoreElements()) {
            if (!"StackTrace".equals(existingLogs.nextElement())) {
               continue;
            }

            STACK_LOG = Logger.getLogger("StackTrace");
            break;
         }

         STACK_LOG = Logger.getLogger("StackTrace");
         STACK_LOG.setUseParentHandlers(false);
         break;
      }

      GROOVY_PACKAGES = System.getProperty("groovy.sanitized.stacktraces", "groovy.,org.codehaus.groovy.,java.,javax.,sun.,gjdk.groovy.,").split("(\\s|,)+");
      tests = new ArrayList();
   }
}
