package org.apache.maven.surefire.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
   private static final Class[] NO_ARGS = new Class[0];
   private static final Object[] NO_ARGS_VALUES = new Object[0];

   public static Method getMethod(Object instance, String methodName, Class[] parameters) {
      return getMethod(instance.getClass(), methodName, parameters);
   }

   public static Method getMethod(Class clazz, String methodName, Class[] parameters) {
      try {
         return clazz.getMethod(methodName, parameters);
      } catch (NoSuchMethodException var4) {
         throw new RuntimeException("When finding method " + methodName, var4);
      }
   }

   public static Method tryGetMethod(Class clazz, String methodName, Class[] parameters) {
      try {
         return clazz.getMethod(methodName, parameters);
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   public static Object invokeGetter(Object instance, String methodName) {
      Method method = getMethod(instance, methodName, NO_ARGS);
      return invokeMethodWithArray(instance, method, NO_ARGS_VALUES);
   }

   public static Constructor getConstructor(Class clazz, Class[] arguments) {
      try {
         return clazz.getConstructor(arguments);
      } catch (NoSuchMethodException var3) {
         throw new SurefireReflectionException(var3);
      }
   }

   public static Object newInstance(Constructor constructor, Object[] params) {
      try {
         return constructor.newInstance(params);
      } catch (InvocationTargetException var3) {
         throw new SurefireReflectionException(var3);
      } catch (InstantiationException var4) {
         throw new SurefireReflectionException(var4);
      } catch (IllegalAccessException var5) {
         throw new SurefireReflectionException(var5);
      }
   }

   public static Object instantiate(ClassLoader classLoader, String classname) {
      try {
         Class clazz = loadClass(classLoader, classname);
         return clazz.newInstance();
      } catch (InstantiationException var3) {
         throw new SurefireReflectionException(var3);
      } catch (IllegalAccessException var4) {
         throw new SurefireReflectionException(var4);
      }
   }

   public static Object instantiateOneArg(ClassLoader classLoader, String className, Class param1Class, Object param1) {
      try {
         Class aClass = loadClass(classLoader, className);
         Constructor constructor = getConstructor(aClass, new Class[]{param1Class});
         return constructor.newInstance(param1);
      } catch (InvocationTargetException var6) {
         throw new SurefireReflectionException(var6.getTargetException());
      } catch (InstantiationException var7) {
         throw new SurefireReflectionException(var7);
      } catch (IllegalAccessException var8) {
         throw new SurefireReflectionException(var8);
      }
   }

   public static Object instantiateTwoArgs(ClassLoader classLoader, String className, Class param1Class, Object param1, Class param2Class, Object param2) {
      try {
         Class aClass = loadClass(classLoader, className);
         Constructor constructor = getConstructor(aClass, new Class[]{param1Class, param2Class});
         return constructor.newInstance(param1, param2);
      } catch (InvocationTargetException var8) {
         throw new SurefireReflectionException(var8.getTargetException());
      } catch (InstantiationException var9) {
         throw new SurefireReflectionException(var9);
      } catch (IllegalAccessException var10) {
         throw new SurefireReflectionException(var10);
      }
   }

   public static void invokeSetter(Object o, String name, Class value1clazz, Object value) {
      Method setter = getMethod(o, name, new Class[]{value1clazz});
      invokeSetter(o, setter, value);
   }

   public static Object invokeSetter(Object target, Method method, Object value) {
      return invokeMethodWithArray(target, method, new Object[]{value});
   }

   public static Object invokeMethodWithArray(Object target, Method method, Object[] args) {
      try {
         return method.invoke(target, args);
      } catch (IllegalAccessException var4) {
         throw new SurefireReflectionException(var4);
      } catch (InvocationTargetException var5) {
         throw new SurefireReflectionException(var5.getTargetException());
      }
   }

   public static Object invokeMethodWithArray2(Object target, Method method, Object[] args) throws InvocationTargetException {
      try {
         return method.invoke(target, args);
      } catch (IllegalAccessException var4) {
         throw new SurefireReflectionException(var4);
      }
   }

   public static Object instantiateObject(String className, Class[] types, Object[] params, ClassLoader classLoader) {
      Class clazz = loadClass(classLoader, className);
      Constructor constructor = getConstructor(clazz, types);
      return newInstance(constructor, params);
   }

   public static Class tryLoadClass(ClassLoader classLoader, String className) {
      try {
         return classLoader.loadClass(className);
      } catch (NoClassDefFoundError var3) {
      } catch (ClassNotFoundException var4) {
      }

      return null;
   }

   public static Class loadClass(ClassLoader classLoader, String className) {
      try {
         return classLoader.loadClass(className);
      } catch (NoClassDefFoundError var3) {
         throw new SurefireReflectionException(var3);
      } catch (ClassNotFoundException var4) {
         throw new SurefireReflectionException(var4);
      }
   }
}
