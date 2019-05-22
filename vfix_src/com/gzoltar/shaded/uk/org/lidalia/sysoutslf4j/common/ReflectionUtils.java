package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class ReflectionUtils {
   public static Object invokeMethod(String methodName, Object target) {
      Method method = getMethod(methodName, target.getClass());
      return invokeMethod(method, target);
   }

   public static Object invokeMethod(String methodName, Object target, Class<?> argType, Object arg) {
      Method method = getMethod(methodName, target.getClass(), argType);
      return invokeMethod(method, target, arg);
   }

   public static Object invokeStaticMethod(String methodName, Class<?> targetClass) {
      Method method = getMethod(methodName, targetClass);
      return invokeMethod(method, targetClass);
   }

   public static Object invokeStaticMethod(String methodName, Class<?> targetClass, Class<?> argType, Object arg) {
      Method method = getMethod(methodName, targetClass, argType);
      return invokeMethod(method, targetClass, arg);
   }

   private static Method getMethod(String methodName, Class<?> classWithMethod, Class<?>... argTypes) {
      try {
         return getMethod(methodName, classWithMethod, argTypes, (NoSuchMethodException)null);
      } catch (NoSuchMethodException var4) {
         throw new WrappedCheckedException(var4);
      }
   }

   private static Method getMethod(String methodName, Class<?> classWithMethod, Class<?>[] argTypes, NoSuchMethodException originalNoSuchMethodException) throws NoSuchMethodException {
      Method foundMethod;
      try {
         foundMethod = classWithMethod.getDeclaredMethod(methodName, argTypes);
      } catch (NoSuchMethodException var8) {
         Class<?> superclass = classWithMethod.getSuperclass();
         if (superclass == null) {
            throw originalNoSuchMethodException;
         }

         NoSuchMethodException firstNoSuchMethodException = (NoSuchMethodException)getDefault(originalNoSuchMethodException, var8);
         foundMethod = getMethod(methodName, superclass, argTypes, firstNoSuchMethodException);
      }

      return foundMethod;
   }

   private static <T> T getDefault(T mightBeNull, T useIfNull) {
      Object result;
      if (mightBeNull == null) {
         result = useIfNull;
      } else {
         result = mightBeNull;
      }

      return result;
   }

   public static Object invokeMethod(final Method method, Object target, Object... args) {
      try {
         if (!method.isAccessible()) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
               public Void run() {
                  method.setAccessible(true);
                  return null;
               }
            });
         }

         return method.invoke(target, args);
      } catch (Exception var4) {
         throw ExceptionUtils.asRuntimeException(var4);
      }
   }

   private ReflectionUtils() {
      throw new UnsupportedOperationException("Not instantiable");
   }

   public static <TypeInThisClassLoader> TypeInThisClassLoader wrap(Object target, Class<TypeInThisClassLoader> interfaceClass) {
      Object result;
      if (interfaceClass.isAssignableFrom(target.getClass())) {
         result = target;
      } else {
         result = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaceClass}, new ProxyingInvocationHandler(target, interfaceClass));
      }

      return result;
   }
}
