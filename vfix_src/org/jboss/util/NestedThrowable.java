package org.jboss.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import org.jboss.logging.Logger;
import org.jboss.util.platform.Java;

public interface NestedThrowable extends Serializable {
   boolean PARENT_TRACE_ENABLED = NestedThrowable.Util.getBoolean("parentTraceEnabled", true);
   boolean NESTED_TRACE_ENABLED = NestedThrowable.Util.getBoolean("nestedTraceEnabled", Java.isCompatible(5) && !PARENT_TRACE_ENABLED || !Java.isCompatible(5));
   boolean DETECT_DUPLICATE_NESTING = NestedThrowable.Util.getBoolean("detectDuplicateNesting", true);

   Throwable getNested();

   Throwable getCause();

   public static final class Util {
      private static Logger pvtLog = null;

      private static Logger getLogger() {
         if (pvtLog == null) {
            pvtLog = Logger.getLogger(NestedThrowable.class);
         }

         return pvtLog;
      }

      protected static boolean getBoolean(String name, boolean defaultValue) {
         name = NestedThrowable.class.getName() + "." + name;
         String value = System.getProperty(name, String.valueOf(defaultValue));
         Logger log = getLogger();
         log.debug(name + "=" + value);
         return new Boolean(value);
      }

      public static void checkNested(NestedThrowable parent, Throwable child) {
         if (NestedThrowable.DETECT_DUPLICATE_NESTING && parent != null && child != null) {
            Class<?> parentType = parent.getClass();
            Class<?> childType = child.getClass();
            if (parentType.isAssignableFrom(childType)) {
               Logger log = getLogger();
               log.warn("Duplicate throwable nesting of same base type: " + parentType + " is assignable from: " + childType);
            }

         }
      }

      public static String getMessage(String msg, Throwable nested) {
         StringBuffer buff = new StringBuffer(msg == null ? "" : msg);
         if (nested != null) {
            buff.append(msg == null ? "- " : "; - ").append("nested throwable: (").append(nested).append(")");
         }

         return buff.toString();
      }

      public static void print(Throwable nested, PrintStream stream) {
         if (stream == null) {
            throw new NullArgumentException("stream");
         } else {
            if (NestedThrowable.NESTED_TRACE_ENABLED && nested != null) {
               synchronized(stream) {
                  if (NestedThrowable.PARENT_TRACE_ENABLED) {
                     stream.print(" + nested throwable: ");
                  } else {
                     stream.print("[ parent trace omitted ]: ");
                  }

                  nested.printStackTrace(stream);
               }
            }

         }
      }

      public static void print(Throwable nested, PrintWriter writer) {
         if (writer == null) {
            throw new NullArgumentException("writer");
         } else {
            if (NestedThrowable.NESTED_TRACE_ENABLED && nested != null) {
               synchronized(writer) {
                  if (NestedThrowable.PARENT_TRACE_ENABLED) {
                     writer.print(" + nested throwable: ");
                  } else {
                     writer.print("[ parent trace omitted ]: ");
                  }

                  nested.printStackTrace(writer);
               }
            }

         }
      }
   }
}
