package org.jboss.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ThrowableHandler {
   protected static List<ThrowableListener> listeners = Collections.synchronizedList(new ArrayList());

   public static void addThrowableListener(ThrowableListener listener) {
      if (!listeners.contains(listener)) {
         listeners.add(listener);
      }

   }

   public static void removeThrowableListener(ThrowableListener listener) {
      listeners.remove(listener);
   }

   protected static void fireOnThrowable(int type, Throwable t) {
      Object[] list = listeners.toArray();

      for(int i = 0; i < list.length; ++i) {
         ((ThrowableListener)list[i]).onThrowable(type, t);
      }

   }

   public static void add(int type, Throwable t) {
      if (t != null) {
         try {
            fireOnThrowable(type, t);
         } catch (Throwable var3) {
            System.err.println("Unable to handle throwable: " + t + " because of:");
            var3.printStackTrace();
         }

      }
   }

   public static void add(Throwable t) {
      add(0, t);
   }

   public static void addError(Throwable t) {
      add(1, t);
   }

   public static void addWarning(Throwable t) {
      add(1, t);
   }

   public interface Type {
      int UNKNOWN = 0;
      int ERROR = 1;
      int WARNING = 2;
   }
}
