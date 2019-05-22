package org.apache.commons.beanutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ConstructorUtils {
   private static final Class[] emptyClassArray = new Class[0];
   private static final Object[] emptyObjectArray = new Object[0];

   public static Object invokeConstructor(Class klass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      Object[] args = new Object[]{arg};
      return invokeConstructor(klass, args);
   }

   public static Object invokeConstructor(Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      if (null == args) {
         args = emptyObjectArray;
      }

      int arguments = args.length;
      Class[] parameterTypes = new Class[arguments];

      for(int i = 0; i < arguments; ++i) {
         parameterTypes[i] = args[i].getClass();
      }

      return invokeConstructor(klass, args, parameterTypes);
   }

   public static Object invokeConstructor(Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      if (parameterTypes == null) {
         parameterTypes = emptyClassArray;
      }

      if (args == null) {
         args = emptyObjectArray;
      }

      Constructor ctor = getMatchingAccessibleConstructor(klass, parameterTypes);
      if (null == ctor) {
         throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
      } else {
         return ctor.newInstance(args);
      }
   }

   public static Object invokeExactConstructor(Class klass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      Object[] args = new Object[]{arg};
      return invokeExactConstructor(klass, args);
   }

   public static Object invokeExactConstructor(Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      if (null == args) {
         args = emptyObjectArray;
      }

      int arguments = args.length;
      Class[] parameterTypes = new Class[arguments];

      for(int i = 0; i < arguments; ++i) {
         parameterTypes[i] = args[i].getClass();
      }

      return invokeExactConstructor(klass, args, parameterTypes);
   }

   public static Object invokeExactConstructor(Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      if (args == null) {
         args = emptyObjectArray;
      }

      if (parameterTypes == null) {
         parameterTypes = emptyClassArray;
      }

      Constructor ctor = getAccessibleConstructor(klass, parameterTypes);
      if (null == ctor) {
         throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
      } else {
         return ctor.newInstance(args);
      }
   }

   public static Constructor getAccessibleConstructor(Class klass, Class parameterType) {
      Class[] parameterTypes = new Class[]{parameterType};
      return getAccessibleConstructor(klass, parameterTypes);
   }

   public static Constructor getAccessibleConstructor(Class klass, Class[] parameterTypes) {
      try {
         return getAccessibleConstructor(klass.getConstructor(parameterTypes));
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   public static Constructor getAccessibleConstructor(Constructor ctor) {
      if (ctor == null) {
         return null;
      } else if (!Modifier.isPublic(ctor.getModifiers())) {
         return null;
      } else {
         Class clazz = ctor.getDeclaringClass();
         return Modifier.isPublic(clazz.getModifiers()) ? ctor : null;
      }
   }

   private static Constructor getMatchingAccessibleConstructor(Class clazz, Class[] parameterTypes) {
      try {
         Constructor ctor = clazz.getConstructor(parameterTypes);

         try {
            ctor.setAccessible(true);
         } catch (SecurityException var13) {
         }

         return ctor;
      } catch (NoSuchMethodException var14) {
         int paramSize = parameterTypes.length;
         Constructor[] ctors = clazz.getConstructors();
         int i = 0;

         for(int size = ctors.length; i < size; ++i) {
            Class[] ctorParams = ctors[i].getParameterTypes();
            int ctorParamSize = ctorParams.length;
            if (ctorParamSize == paramSize) {
               boolean match = true;

               for(int n = 0; n < ctorParamSize; ++n) {
                  if (!MethodUtils.isAssignmentCompatible(ctorParams[n], parameterTypes[n])) {
                     match = false;
                     break;
                  }
               }

               if (match) {
                  Constructor ctor = getAccessibleConstructor(ctors[i]);
                  if (ctor != null) {
                     try {
                        ctor.setAccessible(true);
                     } catch (SecurityException var12) {
                     }

                     return ctor;
                  }
               }
            }
         }

         return null;
      }
   }
}
