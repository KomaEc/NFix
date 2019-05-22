package org.apache.velocity.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ExceptionUtils {
   private static boolean causesAllowed = true;
   // $FF: synthetic field
   static Class class$java$lang$RuntimeException;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public static RuntimeException createRuntimeException(String message, Throwable cause) {
      return (RuntimeException)createWithCause(class$java$lang$RuntimeException == null ? (class$java$lang$RuntimeException = class$("java.lang.RuntimeException")) : class$java$lang$RuntimeException, message, cause);
   }

   public static Throwable createWithCause(Class clazz, String message, Throwable cause) {
      Throwable re = null;
      Constructor constructor;
      if (causesAllowed) {
         try {
            constructor = clazz.getConstructor(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable);
            re = (Throwable)constructor.newInstance(message, cause);
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            causesAllowed = false;
         }
      }

      if (re == null) {
         try {
            constructor = clazz.getConstructor(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            re = (Throwable)constructor.newInstance(message + " caused by " + cause);
         } catch (RuntimeException var5) {
            throw var5;
         } catch (Exception var6) {
            throw new RuntimeException("Error caused " + var6);
         }
      }

      return re;
   }

   public static void setCause(Throwable onObject, Throwable cause) {
      if (causesAllowed) {
         try {
            Method method = onObject.getClass().getMethod("initCause", class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable);
            method.invoke(onObject, cause);
         } catch (RuntimeException var3) {
            throw var3;
         } catch (Exception var4) {
            causesAllowed = false;
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
