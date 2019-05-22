package com.gzoltar.shaded.org.apache.commons.lang3.reflect;

import com.gzoltar.shaded.org.apache.commons.lang3.ArrayUtils;
import com.gzoltar.shaded.org.apache.commons.lang3.ClassUtils;
import com.gzoltar.shaded.org.apache.commons.lang3.Validate;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodUtils {
   public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      Class<?>[] parameterTypes = ClassUtils.toClass(args);
      return invokeMethod(object, methodName, args, parameterTypes);
   }

   public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
      args = ArrayUtils.nullToEmpty(args);
      Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
      } else {
         return method.invoke(object, args);
      }
   }

   public static Object invokeExactMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeExactMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      Class<?>[] parameterTypes = ClassUtils.toClass(args);
      return invokeExactMethod(object, methodName, args, parameterTypes);
   }

   public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
      Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
      } else {
         return method.invoke(object, args);
      }
   }

   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
      Method method = getAccessibleMethod(cls, methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
      } else {
         return method.invoke((Object)null, args);
      }
   }

   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      Class<?>[] parameterTypes = ClassUtils.toClass(args);
      return invokeStaticMethod(cls, methodName, args, parameterTypes);
   }

   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
      Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
      } else {
         return method.invoke((Object)null, args);
      }
   }

   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      args = ArrayUtils.nullToEmpty(args);
      Class<?>[] parameterTypes = ClassUtils.toClass(args);
      return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
   }

   public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
      try {
         return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   public static Method getAccessibleMethod(Method method) {
      if (!MemberUtils.isAccessible(method)) {
         return null;
      } else {
         Class<?> cls = method.getDeclaringClass();
         if (Modifier.isPublic(cls.getModifiers())) {
            return method;
         } else {
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
            if (method == null) {
               method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
            }

            return method;
         }
      }
   }

   private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
      for(Class parentClass = cls.getSuperclass(); parentClass != null; parentClass = parentClass.getSuperclass()) {
         if (Modifier.isPublic(parentClass.getModifiers())) {
            try {
               return parentClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
               return null;
            }
         }
      }

      return null;
   }

   private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
      while(cls != null) {
         Class<?>[] interfaces = cls.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            if (Modifier.isPublic(interfaces[i].getModifiers())) {
               try {
                  return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
               } catch (NoSuchMethodException var6) {
                  Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                  if (method != null) {
                     return method;
                  }
               }
            }
         }

         cls = cls.getSuperclass();
      }

      return null;
   }

   public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
      Method bestMatch;
      try {
         bestMatch = cls.getMethod(methodName, parameterTypes);
         MemberUtils.setAccessibleWorkaround(bestMatch);
         return bestMatch;
      } catch (NoSuchMethodException var10) {
         bestMatch = null;
         Method[] methods = cls.getMethods();
         Method[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (method.getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
               Method accessibleMethod = getAccessibleMethod(method);
               if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                  bestMatch = accessibleMethod;
               }
            }
         }

         if (bestMatch != null) {
            MemberUtils.setAccessibleWorkaround(bestMatch);
         }

         return bestMatch;
      }
   }

   public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfacesBehavior) {
      Validate.notNull(method);
      Set<Method> result = new LinkedHashSet();
      result.add(method);
      Class<?>[] parameterTypes = method.getParameterTypes();
      Class<?> declaringClass = method.getDeclaringClass();
      Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
      hierarchy.next();

      while(true) {
         label32:
         while(true) {
            Method m;
            do {
               if (!hierarchy.hasNext()) {
                  return result;
               }

               Class<?> c = (Class)hierarchy.next();
               m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
            } while(m == null);

            if (Arrays.equals(m.getParameterTypes(), parameterTypes)) {
               result.add(m);
            } else {
               Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());

               for(int i = 0; i < parameterTypes.length; ++i) {
                  Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
                  Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
                  if (!TypeUtils.equals(childType, parentType)) {
                     continue label32;
                  }
               }

               result.add(m);
            }
         }
      }
   }

   public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
      List<Method> annotatedMethodsList = getMethodsListWithAnnotation(cls, annotationCls);
      return (Method[])annotatedMethodsList.toArray(new Method[annotatedMethodsList.size()]);
   }

   public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
      Validate.isTrue(cls != null, "The class must not be null");
      Validate.isTrue(annotationCls != null, "The annotation class must not be null");
      Method[] allMethods = cls.getMethods();
      List<Method> annotatedMethods = new ArrayList();
      Method[] arr$ = allMethods;
      int len$ = allMethods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         if (method.getAnnotation(annotationCls) != null) {
            annotatedMethods.add(method);
         }
      }

      return annotatedMethods;
   }
}
