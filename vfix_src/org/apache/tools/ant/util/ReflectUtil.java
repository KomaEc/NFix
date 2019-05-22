package org.apache.tools.ant.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;

public class ReflectUtil {
   private ReflectUtil() {
   }

   public static Object invoke(Object obj, String methodName) {
      try {
         Method method = obj.getClass().getMethod(methodName, (Class[])null);
         return method.invoke(obj, (Object[])null);
      } catch (Exception var3) {
         throwBuildException(var3);
         return null;
      }
   }

   public static Object invoke(Object obj, String methodName, Class argType, Object arg) {
      try {
         Method method = obj.getClass().getMethod(methodName, argType);
         return method.invoke(obj, arg);
      } catch (Exception var5) {
         throwBuildException(var5);
         return null;
      }
   }

   public static Object invoke(Object obj, String methodName, Class argType1, Object arg1, Class argType2, Object arg2) {
      try {
         Method method = obj.getClass().getMethod(methodName, argType1, argType2);
         return method.invoke(obj, arg1, arg2);
      } catch (Exception var7) {
         throwBuildException(var7);
         return null;
      }
   }

   public static Object getField(Object obj, String fieldName) throws BuildException {
      try {
         Field field = obj.getClass().getDeclaredField(fieldName);
         field.setAccessible(true);
         return field.get(obj);
      } catch (Exception var3) {
         throwBuildException(var3);
         return null;
      }
   }

   public static void throwBuildException(Exception t) throws BuildException {
      if (t instanceof InvocationTargetException) {
         Throwable t2 = ((InvocationTargetException)t).getTargetException();
         if (t2 instanceof BuildException) {
            throw (BuildException)t2;
         } else {
            throw new BuildException(t2);
         }
      } else {
         throw new BuildException(t);
      }
   }
}
