package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.WeakHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MethodUtils {
   private static Log log;
   private static boolean loggedAccessibleWarning;
   private static final Class[] emptyClassArray;
   private static final Object[] emptyObjectArray;
   private static WeakHashMap cache;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$MethodUtils;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Character;

   public static Object invokeMethod(Object object, String methodName, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      Object[] args = new Object[]{arg};
      return invokeMethod(object, methodName, args);
   }

   public static Object invokeMethod(Object object, String methodName, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      if (args == null) {
         args = emptyObjectArray;
      }

      int arguments = args.length;
      Class[] parameterTypes = new Class[arguments];

      for(int i = 0; i < arguments; ++i) {
         parameterTypes[i] = args[i].getClass();
      }

      return invokeMethod(object, methodName, args, parameterTypes);
   }

   public static Object invokeMethod(Object object, String methodName, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      if (parameterTypes == null) {
         parameterTypes = emptyClassArray;
      }

      if (args == null) {
         args = emptyObjectArray;
      }

      Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
      } else {
         return method.invoke(object, args);
      }
   }

   public static Object invokeExactMethod(Object object, String methodName, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      Object[] args = new Object[]{arg};
      return invokeExactMethod(object, methodName, args);
   }

   public static Object invokeExactMethod(Object object, String methodName, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      if (args == null) {
         args = emptyObjectArray;
      }

      int arguments = args.length;
      Class[] parameterTypes = new Class[arguments];

      for(int i = 0; i < arguments; ++i) {
         parameterTypes[i] = args[i].getClass();
      }

      return invokeExactMethod(object, methodName, args, parameterTypes);
   }

   public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      if (args == null) {
         args = emptyObjectArray;
      }

      if (parameterTypes == null) {
         parameterTypes = emptyClassArray;
      }

      Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
      if (method == null) {
         throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
      } else {
         return method.invoke(object, args);
      }
   }

   public static Method getAccessibleMethod(Class clazz, String methodName, Class parameterType) {
      Class[] parameterTypes = new Class[]{parameterType};
      return getAccessibleMethod(clazz, methodName, parameterTypes);
   }

   public static Method getAccessibleMethod(Class clazz, String methodName, Class[] parameterTypes) {
      try {
         MethodUtils.MethodDescriptor md = new MethodUtils.MethodDescriptor(clazz, methodName, parameterTypes, true);
         Method method = (Method)cache.get(md);
         if (method != null) {
            return method;
         } else {
            method = getAccessibleMethod(clazz.getMethod(methodName, parameterTypes));
            cache.put(md, method);
            return method;
         }
      } catch (NoSuchMethodException var5) {
         return null;
      }
   }

   public static Method getAccessibleMethod(Method method) {
      if (method == null) {
         return null;
      } else if (!Modifier.isPublic(method.getModifiers())) {
         return null;
      } else {
         Class clazz = method.getDeclaringClass();
         if (Modifier.isPublic(clazz.getModifiers())) {
            return method;
         } else {
            method = getAccessibleMethodFromInterfaceNest(clazz, method.getName(), method.getParameterTypes());
            return method;
         }
      }
   }

   private static Method getAccessibleMethodFromInterfaceNest(Class clazz, String methodName, Class[] parameterTypes) {
      Method method;
      for(method = null; clazz != null; clazz = clazz.getSuperclass()) {
         Class[] interfaces = clazz.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            if (Modifier.isPublic(interfaces[i].getModifiers())) {
               try {
                  method = interfaces[i].getDeclaredMethod(methodName, parameterTypes);
               } catch (NoSuchMethodException var7) {
               }

               if (method != null) {
                  break;
               }

               method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
               if (method != null) {
                  break;
               }
            }
         }
      }

      if (method != null) {
         return method;
      } else {
         return null;
      }
   }

   public static Method getMatchingAccessibleMethod(Class clazz, String methodName, Class[] parameterTypes) {
      if (log.isTraceEnabled()) {
         log.trace("Matching name=" + methodName + " on " + clazz);
      }

      MethodUtils.MethodDescriptor md = new MethodUtils.MethodDescriptor(clazz, methodName, parameterTypes, false);

      try {
         Method method = (Method)cache.get(md);
         if (method != null) {
            return method;
         } else {
            method = clazz.getMethod(methodName, parameterTypes);
            if (log.isTraceEnabled()) {
               log.trace("Found straight match: " + method);
               log.trace("isPublic:" + Modifier.isPublic(method.getModifiers()));
            }

            try {
               method.setAccessible(true);
            } catch (SecurityException var16) {
               if (!loggedAccessibleWarning) {
                  boolean vunerableJVM = false;

                  try {
                     String specVersion = System.getProperty("java.specification.version");
                     if (specVersion.charAt(0) == '1' && (specVersion.charAt(0) == '0' || specVersion.charAt(0) == '1' || specVersion.charAt(0) == '2' || specVersion.charAt(0) == '3')) {
                        vunerableJVM = true;
                     }
                  } catch (SecurityException var15) {
                     vunerableJVM = true;
                  }

                  if (vunerableJVM) {
                     log.warn("Current Security Manager restricts use of workarounds for reflection bugs  in pre-1.4 JVMs.");
                  }

                  loggedAccessibleWarning = true;
               }

               log.debug("Cannot setAccessible on method. Therefore cannot use jvm access bug workaround.", var16);
            }

            cache.put(md, method);
            return method;
         }
      } catch (NoSuchMethodException var17) {
         int paramSize = parameterTypes.length;
         Method[] methods = clazz.getMethods();
         int i = 0;

         for(int size = methods.length; i < size; ++i) {
            if (methods[i].getName().equals(methodName)) {
               if (log.isTraceEnabled()) {
                  log.trace("Found matching name:");
                  log.trace(methods[i]);
               }

               Class[] methodsParams = methods[i].getParameterTypes();
               int methodParamSize = methodsParams.length;
               if (methodParamSize == paramSize) {
                  boolean match = true;

                  for(int n = 0; n < methodParamSize; ++n) {
                     if (log.isTraceEnabled()) {
                        log.trace("Param=" + parameterTypes[n].getName());
                        log.trace("Method=" + methodsParams[n].getName());
                     }

                     if (!isAssignmentCompatible(methodsParams[n], parameterTypes[n])) {
                        if (log.isTraceEnabled()) {
                           log.trace(methodsParams[n] + " is not assignable from " + parameterTypes[n]);
                        }

                        match = false;
                        break;
                     }
                  }

                  if (match) {
                     Method method = getAccessibleMethod(methods[i]);
                     if (method != null) {
                        if (log.isTraceEnabled()) {
                           log.trace(method + " accessible version of " + methods[i]);
                        }

                        try {
                           method.setAccessible(true);
                        } catch (SecurityException var14) {
                           if (!loggedAccessibleWarning) {
                              log.warn("Cannot use JVM pre-1.4 access bug workaround due to restrictive security manager.");
                              loggedAccessibleWarning = true;
                           }

                           log.debug("Cannot setAccessible on method. Therefore cannot use jvm access bug workaround.", var14);
                        }

                        cache.put(md, method);
                        return method;
                     }

                     log.trace("Couldn't find accessible method.");
                  }
               }
            }
         }

         log.trace("No match found.");
         return null;
      }
   }

   public static final boolean isAssignmentCompatible(Class parameterType, Class parameterization) {
      if (parameterType.isAssignableFrom(parameterization)) {
         return true;
      } else {
         if (parameterType.isPrimitive()) {
            Class parameterWrapperClazz = getPrimitiveWrapper(parameterType);
            if (parameterWrapperClazz != null) {
               return parameterWrapperClazz.equals(parameterization);
            }
         }

         return false;
      }
   }

   public static Class getPrimitiveWrapper(Class primitiveType) {
      if (Boolean.TYPE.equals(primitiveType)) {
         return class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean;
      } else if (Float.TYPE.equals(primitiveType)) {
         return class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float;
      } else if (Long.TYPE.equals(primitiveType)) {
         return class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long;
      } else if (Integer.TYPE.equals(primitiveType)) {
         return class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer;
      } else if (Short.TYPE.equals(primitiveType)) {
         return class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short;
      } else if (Byte.TYPE.equals(primitiveType)) {
         return class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte;
      } else if (Double.TYPE.equals(primitiveType)) {
         return class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double;
      } else if (Character.TYPE.equals(primitiveType)) {
         return class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character;
      } else {
         return null;
      }
   }

   public static Class getPrimitiveType(Class wrapperType) {
      if ((class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean).equals(wrapperType)) {
         return Boolean.TYPE;
      } else if ((class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float).equals(wrapperType)) {
         return Float.TYPE;
      } else if ((class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long).equals(wrapperType)) {
         return Long.TYPE;
      } else if ((class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer).equals(wrapperType)) {
         return Integer.TYPE;
      } else if ((class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short).equals(wrapperType)) {
         return Short.TYPE;
      } else if ((class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte).equals(wrapperType)) {
         return Byte.TYPE;
      } else if ((class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double).equals(wrapperType)) {
         return Double.TYPE;
      } else if ((class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character).equals(wrapperType)) {
         return Character.TYPE;
      } else {
         if (log.isDebugEnabled()) {
            log.debug("Not a known primitive wrapper class: " + wrapperType);
         }

         return null;
      }
   }

   public static Class toNonPrimitiveClass(Class clazz) {
      if (clazz.isPrimitive()) {
         Class primitiveClazz = getPrimitiveWrapper(clazz);
         return primitiveClazz != null ? primitiveClazz : clazz;
      } else {
         return clazz;
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

   static {
      log = LogFactory.getLog(class$org$apache$commons$beanutils$MethodUtils == null ? (class$org$apache$commons$beanutils$MethodUtils = class$("org.apache.commons.beanutils.MethodUtils")) : class$org$apache$commons$beanutils$MethodUtils);
      loggedAccessibleWarning = false;
      emptyClassArray = new Class[0];
      emptyObjectArray = new Object[0];
      cache = new WeakHashMap();
   }

   private static class MethodDescriptor {
      private Class cls;
      private String methodName;
      private Class[] paramTypes;
      private boolean exact;
      private int hashCode;

      public MethodDescriptor(Class cls, String methodName, Class[] paramTypes, boolean exact) {
         if (cls == null) {
            throw new IllegalArgumentException("Class cannot be null");
         } else if (methodName == null) {
            throw new IllegalArgumentException("Method Name cannot be null");
         } else {
            if (paramTypes == null) {
               paramTypes = MethodUtils.emptyClassArray;
            }

            this.cls = cls;
            this.methodName = methodName;
            this.paramTypes = paramTypes;
            this.exact = exact;
            this.hashCode = methodName.length();
         }
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof MethodUtils.MethodDescriptor)) {
            return false;
         } else {
            MethodUtils.MethodDescriptor md = (MethodUtils.MethodDescriptor)obj;
            return this.exact == md.exact && this.methodName.equals(md.methodName) && this.cls.equals(md.cls) && Arrays.equals(this.paramTypes, md.paramTypes);
         }
      }

      public int hashCode() {
         return this.hashCode;
      }
   }
}
