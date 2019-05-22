package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaMethod;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ParameterTypes;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.wrappers.Wrapper;
import org.codehaus.groovy.util.FastArray;

public class MetaClassHelper {
   public static final Object[] EMPTY_ARRAY = new Object[0];
   public static final Class[] EMPTY_TYPE_ARRAY = new Class[0];
   public static final Object[] ARRAY_WITH_NULL = new Object[]{null};
   protected static final Logger LOG = Logger.getLogger(MetaClassHelper.class.getName());
   private static final int MAX_ARG_LEN = 12;
   private static final int OBJECT_SHIFT = 23;
   private static final int INTERFACE_SHIFT = 0;
   private static final int PRIMITIVE_SHIFT = 21;
   private static final int VARGS_SHIFT = 44;
   public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
   private static final Class[] PRIMITIVES;
   private static final int[][] PRIMITIVE_DISTANCE_TABLE;

   public static boolean accessibleToConstructor(Class at, Constructor constructor) {
      boolean accessible = false;
      int modifiers = constructor.getModifiers();
      if (Modifier.isPublic(modifiers)) {
         accessible = true;
      } else if (Modifier.isPrivate(modifiers)) {
         accessible = at.getName().equals(constructor.getName());
      } else {
         Boolean isAccessible;
         if (Modifier.isProtected(modifiers)) {
            isAccessible = checkCompatiblePackages(at, constructor);
            if (isAccessible != null) {
               accessible = isAccessible;
            } else {
               boolean flag = false;

               for(Class clazz = at; !flag && clazz != null; clazz = clazz.getSuperclass()) {
                  if (clazz.equals(constructor.getDeclaringClass())) {
                     flag = true;
                     break;
                  }

                  if (clazz.equals(Object.class)) {
                     break;
                  }
               }

               accessible = flag;
            }
         } else {
            isAccessible = checkCompatiblePackages(at, constructor);
            if (isAccessible != null) {
               accessible = isAccessible;
            }
         }
      }

      return accessible;
   }

   private static Boolean checkCompatiblePackages(Class at, Constructor constructor) {
      if (at.getPackage() == null && constructor.getDeclaringClass().getPackage() == null) {
         return Boolean.TRUE;
      } else if (at.getPackage() == null && constructor.getDeclaringClass().getPackage() != null) {
         return Boolean.FALSE;
      } else if (at.getPackage() != null && constructor.getDeclaringClass().getPackage() == null) {
         return Boolean.FALSE;
      } else {
         return at.getPackage().equals(constructor.getDeclaringClass().getPackage()) ? Boolean.TRUE : null;
      }
   }

   public static Object[] asWrapperArray(Object parameters, Class componentType) {
      Object[] ret = null;
      int i;
      if (componentType == Boolean.TYPE) {
         boolean[] array = (boolean[])((boolean[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Character.TYPE) {
         char[] array = (char[])((char[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Byte.TYPE) {
         byte[] array = (byte[])((byte[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Integer.TYPE) {
         int[] array = (int[])((int[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Short.TYPE) {
         short[] array = (short[])((short[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Long.TYPE) {
         long[] array = (long[])((long[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Double.TYPE) {
         double[] array = (double[])((double[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      } else if (componentType == Float.TYPE) {
         float[] array = (float[])((float[])parameters);
         ret = new Object[array.length];

         for(i = 0; i < array.length; ++i) {
            ret[i] = array[i];
         }
      }

      return ret;
   }

   public static Object asPrimitiveArray(List list, Class parameterType) {
      Class arrayType = parameterType.getComponentType();
      Object objArray = Array.newInstance(arrayType, list.size());

      for(int i = 0; i < list.size(); ++i) {
         Object obj = list.get(i);
         if (arrayType.isPrimitive()) {
            if (obj instanceof Integer) {
               Array.setInt(objArray, i, (Integer)obj);
            } else if (obj instanceof Double) {
               Array.setDouble(objArray, i, (Double)obj);
            } else if (obj instanceof Boolean) {
               Array.setBoolean(objArray, i, (Boolean)obj);
            } else if (obj instanceof Long) {
               Array.setLong(objArray, i, (Long)obj);
            } else if (obj instanceof Float) {
               Array.setFloat(objArray, i, (Float)obj);
            } else if (obj instanceof Character) {
               Array.setChar(objArray, i, (Character)obj);
            } else if (obj instanceof Byte) {
               Array.setByte(objArray, i, (Byte)obj);
            } else if (obj instanceof Short) {
               Array.setShort(objArray, i, (Short)obj);
            }
         } else {
            Array.set(objArray, i, obj);
         }
      }

      return objArray;
   }

   private static int getPrimitiveIndex(Class c) {
      for(byte i = 0; i < PRIMITIVES.length; ++i) {
         if (PRIMITIVES[i] == c) {
            return i;
         }
      }

      return -1;
   }

   private static int getPrimitiveDistance(Class from, Class to) {
      int fromIndex = getPrimitiveIndex(from);
      int toIndex = getPrimitiveIndex(to);
      return fromIndex != -1 && toIndex != -1 ? PRIMITIVE_DISTANCE_TABLE[toIndex][fromIndex] : -1;
   }

   private static int getMaximumInterfaceDistance(Class c, Class interfaceClass) {
      if (c == null) {
         return -1;
      } else if (c == interfaceClass) {
         return 0;
      } else {
         Class[] interfaces = c.getInterfaces();
         int max = -1;
         Class[] arr$ = interfaces;
         int len$ = interfaces.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class anInterface = arr$[i$];
            int sub = getMaximumInterfaceDistance(anInterface, interfaceClass);
            if (sub != -1) {
               ++sub;
            }

            max = Math.max(max, sub);
         }

         int superClassMax = getMaximumInterfaceDistance(c.getSuperclass(), interfaceClass);
         return Math.max(max, superClassMax);
      }
   }

   private static long calculateParameterDistance(Class argument, CachedClass parameter) {
      if (parameter.getTheClass() == argument) {
         return 0L;
      } else if (parameter.isInterface()) {
         return (long)(getMaximumInterfaceDistance(argument, parameter.getTheClass()) << 0);
      } else {
         long objectDistance = 0L;
         if (argument != null) {
            long pd = (long)getPrimitiveDistance(parameter.getTheClass(), argument);
            if (pd != -1L) {
               return pd << 21;
            }

            objectDistance += (long)(PRIMITIVES.length + 1);

            for(Class clazz = ReflectionCache.autoboxType(argument); clazz != null && clazz != parameter.getTheClass(); objectDistance += 3L) {
               if (clazz == GString.class && parameter.getTheClass() == String.class) {
                  objectDistance += 2L;
                  break;
               }

               clazz = clazz.getSuperclass();
            }
         } else {
            Class clazz = parameter.getTheClass();
            if (clazz.isPrimitive()) {
               objectDistance += 2L;
            } else {
               while(clazz != Object.class) {
                  clazz = clazz.getSuperclass();
                  objectDistance += 2L;
               }
            }
         }

         return objectDistance << 23;
      }
   }

   public static long calculateParameterDistance(Class[] arguments, ParameterTypes pt) {
      CachedClass[] parameters = pt.getParameterTypes();
      if (parameters.length == 0) {
         return 0L;
      } else {
         long ret = 0L;
         int noVargsLength = parameters.length - 1;

         for(int i = 0; i < noVargsLength; ++i) {
            ret += calculateParameterDistance(arguments[i], parameters[i]);
         }

         CachedClass vargsType;
         if (arguments.length == parameters.length) {
            vargsType = parameters[noVargsLength];
            if (!parameters[noVargsLength].isAssignableFrom(arguments[noVargsLength])) {
               vargsType = ReflectionCache.getCachedClass(vargsType.getTheClass().getComponentType());
               ret += 35184372088832L;
            }

            ret += calculateParameterDistance(arguments[noVargsLength], vargsType);
         } else if (arguments.length > parameters.length) {
            ret += 2L + (long)arguments.length - (long)parameters.length << 44;
            vargsType = ReflectionCache.getCachedClass(parameters[noVargsLength].getTheClass().getComponentType());

            for(int i = noVargsLength; i < arguments.length; ++i) {
               ret += calculateParameterDistance(arguments[i], vargsType);
            }
         } else {
            ret += 17592186044416L;
         }

         return ret;
      }
   }

   public static String capitalize(String property) {
      String rest = property.substring(1);
      return Character.isLowerCase(property.charAt(0)) && rest.length() > 0 && Character.isUpperCase(rest.charAt(0)) ? property : property.substring(0, 1).toUpperCase() + rest;
   }

   public static Object chooseEmptyMethodParams(FastArray methods) {
      Object vargsMethod = null;
      int len = methods.size();
      Object[] data = methods.getArray();

      for(int i = 0; i != len; ++i) {
         Object method = data[i];
         ParameterTypes pt = (ParameterTypes)method;
         CachedClass[] paramTypes = pt.getParameterTypes();
         int paramLength = paramTypes.length;
         if (paramLength == 0) {
            return method;
         }

         if (paramLength == 1 && pt.isVargsMethod(EMPTY_ARRAY)) {
            vargsMethod = method;
         }
      }

      return vargsMethod;
   }

   public static Object chooseMostGeneralMethodWith1NullParam(FastArray methods) {
      CachedClass closestClass = null;
      CachedClass closestVargsClass = null;
      Object answer = null;
      int closestDist = -1;
      int len = methods.size();

      for(int i = 0; i != len; ++i) {
         Object[] data = methods.getArray();
         Object method = data[i];
         ParameterTypes pt = (ParameterTypes)method;
         CachedClass[] paramTypes = pt.getParameterTypes();
         int paramLength = paramTypes.length;
         if (paramLength != 0 && paramLength <= 2) {
            CachedClass theType = paramTypes[0];
            if (!theType.isPrimitive) {
               if (paramLength == 2) {
                  if (pt.isVargsMethod(ARRAY_WITH_NULL)) {
                     if (closestClass == null) {
                        closestVargsClass = paramTypes[1];
                        closestClass = theType;
                        answer = method;
                     } else if (closestClass.getTheClass() == theType.getTheClass()) {
                        if (closestVargsClass != null) {
                           CachedClass newVargsClass = paramTypes[1];
                           if (isAssignableFrom(newVargsClass.getTheClass(), closestVargsClass.getTheClass())) {
                              closestVargsClass = newVargsClass;
                              answer = method;
                           }
                        }
                     } else if (isAssignableFrom(theType.getTheClass(), closestClass.getTheClass())) {
                        closestVargsClass = paramTypes[1];
                        closestClass = theType;
                        answer = method;
                     }
                  }
               } else if (closestClass != null && !isAssignableFrom(theType.getTheClass(), closestClass.getTheClass())) {
                  if (closestDist == -1) {
                     closestDist = closestClass.getSuperClassDistance();
                  }

                  int newDist = theType.getSuperClassDistance();
                  if (newDist < closestDist) {
                     closestDist = newDist;
                     closestVargsClass = null;
                     closestClass = theType;
                     answer = method;
                  }
               } else {
                  closestVargsClass = null;
                  closestClass = theType;
                  answer = method;
                  closestDist = -1;
               }
            }
         }
      }

      return answer;
   }

   private static int calculateSimplifiedClassDistanceToObject(Class clazz) {
      int objectDistance;
      for(objectDistance = 0; clazz != null; ++objectDistance) {
         clazz = clazz.getSuperclass();
      }

      return objectDistance;
   }

   public static boolean containsMatchingMethod(List list, MetaMethod method) {
      Iterator i$ = list.iterator();

      boolean matches;
      do {
         CachedClass[] params1;
         CachedClass[] params2;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            Object aList = i$.next();
            MetaMethod aMethod = (MetaMethod)aList;
            params1 = aMethod.getParameterTypes();
            params2 = method.getParameterTypes();
         } while(params1.length != params2.length);

         matches = true;

         for(int i = 0; i < params1.length; ++i) {
            if (params1[i] != params2[i]) {
               matches = false;
               break;
            }
         }
      } while(!matches);

      return true;
   }

   public static Class[] convertToTypeArray(Object[] args) {
      if (args == null) {
         return null;
      } else {
         int s = args.length;
         Class[] ans = new Class[s];

         for(int i = 0; i < s; ++i) {
            Object o = args[i];
            if (o == null) {
               ans[i] = null;
            } else if (o instanceof Wrapper) {
               ans[i] = ((Wrapper)o).getType();
            } else {
               ans[i] = o.getClass();
            }
         }

         return ans;
      }
   }

   public static Object makeCommonArray(Object[] arguments, int offset, Class fallback) {
      Class baseClass = null;

      int tmpCount;
      for(tmpCount = offset; tmpCount < arguments.length; ++tmpCount) {
         if (arguments[tmpCount] != null) {
            Class argClass = arguments[tmpCount].getClass();
            if (baseClass == null) {
               baseClass = argClass;
            } else {
               while(baseClass != Object.class && !baseClass.isAssignableFrom(argClass)) {
                  baseClass = baseClass.getSuperclass();
               }
            }
         }
      }

      if (baseClass == null) {
         baseClass = fallback;
      }

      if (baseClass == Object.class && fallback.isInterface()) {
         tmpCount = 0;

         for(int i = offset; i < arguments.length; ++i) {
            if (arguments[i] != null) {
               Set<Class> intfs = new HashSet();

               for(Class tmpClass = arguments[i].getClass(); tmpClass != Object.class; tmpClass = tmpClass.getSuperclass()) {
                  intfs.addAll(Arrays.asList(tmpClass.getInterfaces()));
               }

               if (intfs.contains(fallback)) {
                  ++tmpCount;
               }
            }
         }

         if (tmpCount == arguments.length - offset) {
            baseClass = fallback;
         }
      }

      Object result = makeArray((Object)null, baseClass, arguments.length - offset);
      System.arraycopy(arguments, offset, result, 0, arguments.length - offset);
      return result;
   }

   public static Object makeArray(Object obj, Class secondary, int length) {
      Class baseClass = secondary;
      if (obj != null) {
         baseClass = obj.getClass();
      }

      return Array.newInstance(baseClass, length);
   }

   public static GroovyRuntimeException createExceptionText(String init, MetaMethod method, Object object, Object[] args, Throwable reason, boolean setReason) {
      return new GroovyRuntimeException(init + method + " on: " + object + " with arguments: " + InvokerHelper.toString(args) + " reason: " + reason, setReason ? reason : null);
   }

   protected static String getClassName(Object object) {
      if (object == null) {
         return null;
      } else {
         return object instanceof Class ? ((Class)object).getName() : object.getClass().getName();
      }
   }

   public static Closure getMethodPointer(Object object, String methodName) {
      return new MethodClosure(object, methodName);
   }

   public static boolean isAssignableFrom(Class classToTransformTo, Class classToTransformFrom) {
      if (classToTransformTo == classToTransformFrom) {
         return true;
      } else if (classToTransformFrom == null) {
         return true;
      } else if (classToTransformTo == Object.class) {
         return true;
      } else {
         classToTransformTo = ReflectionCache.autoboxType(classToTransformTo);
         classToTransformFrom = ReflectionCache.autoboxType(classToTransformFrom);
         if (classToTransformTo == classToTransformFrom) {
            return true;
         } else {
            if (classToTransformTo == Integer.class) {
               if (classToTransformFrom == Integer.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == BigInteger.class) {
                  return true;
               }
            } else if (classToTransformTo == Double.class) {
               if (classToTransformFrom == Double.class || classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == Float.class || classToTransformFrom == BigDecimal.class || classToTransformFrom == BigInteger.class) {
                  return true;
               }
            } else if (classToTransformTo == BigDecimal.class) {
               if (classToTransformFrom == Double.class || classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == Float.class || classToTransformFrom == BigDecimal.class || classToTransformFrom == BigInteger.class) {
                  return true;
               }
            } else if (classToTransformTo == BigInteger.class) {
               if (classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == BigInteger.class) {
                  return true;
               }
            } else if (classToTransformTo == Long.class) {
               if (classToTransformFrom == Long.class || classToTransformFrom == Integer.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class) {
                  return true;
               }
            } else if (classToTransformTo == Float.class) {
               if (classToTransformFrom == Float.class || classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class) {
                  return true;
               }
            } else if (classToTransformTo == Short.class) {
               if (classToTransformFrom == Short.class || classToTransformFrom == Byte.class) {
                  return true;
               }
            } else if (classToTransformTo == String.class && (classToTransformFrom == String.class || GString.class.isAssignableFrom(classToTransformFrom))) {
               return true;
            }

            return ReflectionCache.isAssignableFrom(classToTransformTo, classToTransformFrom);
         }
      }
   }

   public static boolean isGenericSetMethod(MetaMethod method) {
      return method.getName().equals("set") && method.getParameterTypes().length == 2;
   }

   protected static boolean isSuperclass(Class claszz, Class superclass) {
      while(claszz != null) {
         if (claszz == superclass) {
            return true;
         }

         claszz = claszz.getSuperclass();
      }

      return false;
   }

   public static boolean parametersAreCompatible(Class[] arguments, Class[] parameters) {
      if (arguments.length != parameters.length) {
         return false;
      } else {
         for(int i = 0; i < arguments.length; ++i) {
            if (!isAssignableFrom(parameters[i], arguments[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static void logMethodCall(Object object, String methodName, Object[] arguments) {
      String className = getClassName(object);
      String logname = "methodCalls." + className + "." + methodName;
      Logger objLog = Logger.getLogger(logname);
      if (objLog.isLoggable(Level.FINER)) {
         StringBuffer msg = new StringBuffer(methodName);
         msg.append("(");
         if (arguments != null) {
            int i = 0;

            while(i < arguments.length) {
               msg.append(normalizedValue(arguments[i]));
               ++i;
               if (i < arguments.length) {
                  msg.append(",");
               }
            }
         }

         msg.append(")");
         objLog.logp(Level.FINER, className, msg.toString(), "called from MetaClass.invokeMethod");
      }
   }

   protected static String normalizedValue(Object argument) {
      String value;
      try {
         value = argument.toString();
         if (value.length() > 12) {
            value = value.substring(0, 10) + "..";
         }

         if (argument instanceof String) {
            value = "'" + value + "'";
         }
      } catch (Exception var3) {
         value = shortName(argument);
      }

      return value;
   }

   protected static String shortName(Object object) {
      if (object != null && object.getClass() != null) {
         String name = getClassName(object);
         if (name == null) {
            return "unknownClassName";
         } else {
            int lastDotPos = name.lastIndexOf(46);
            return lastDotPos >= 0 && lastDotPos < name.length() - 1 ? name.substring(lastDotPos + 1) : name;
         }
      } else {
         return "unknownClass";
      }
   }

   public static Class[] wrap(Class[] classes) {
      Class[] wrappedArguments = new Class[classes.length];

      for(int i = 0; i < wrappedArguments.length; ++i) {
         Class c = classes[i];
         if (c != null) {
            if (c.isPrimitive()) {
               if (c == Integer.TYPE) {
                  c = Integer.class;
               } else if (c == Byte.TYPE) {
                  c = Byte.class;
               } else if (c == Long.TYPE) {
                  c = Long.class;
               } else if (c == Double.TYPE) {
                  c = Double.class;
               } else if (c == Float.TYPE) {
                  c = Float.class;
               }
            } else if (isSuperclass(c, GString.class)) {
               c = String.class;
            }

            wrappedArguments[i] = c;
         }
      }

      return wrappedArguments;
   }

   public static boolean sameClasses(Class[] params, Object[] arguments, boolean weakNullCheck) {
      if (params.length != arguments.length) {
         return false;
      } else {
         for(int i = params.length - 1; i >= 0; --i) {
            Object arg = arguments[i];
            if (arg == null) {
               if (!weakNullCheck) {
                  return false;
               }
            } else if (params[i] != arg.getClass() && (!(arg instanceof Wrapper) || params[i] != ((Wrapper)arg).getType())) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean sameClasses(Class[] params, Object[] arguments) {
      if (params.length != arguments.length) {
         return false;
      } else {
         for(int i = params.length - 1; i >= 0; --i) {
            Object arg = arguments[i];
            if (arg == null) {
               if (params[i] != null) {
                  return false;
               }
            } else if (params[i] != arg.getClass() && (!(arg instanceof Wrapper) || params[i] != ((Wrapper)arg).getType())) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean sameClasses(Class[] params) {
      return params.length == 0;
   }

   public static boolean sameClasses(Class[] params, Object arg1) {
      if (params.length != 1) {
         return false;
      } else {
         return arg1 != null && (params[0] == arg1.getClass() || arg1 instanceof Wrapper && params[0] == ((Wrapper)arg1).getType());
      }
   }

   public static boolean sameClasses(Class[] params, Object arg1, Object arg2) {
      if (params.length != 2) {
         return false;
      } else if (arg1 != null && (params[0] == arg1.getClass() || arg1 instanceof Wrapper && params[0] == ((Wrapper)arg1).getType())) {
         return arg2 != null && (params[1] == arg2.getClass() || arg2 instanceof Wrapper && params[1] == ((Wrapper)arg2).getType());
      } else {
         return false;
      }
   }

   public static boolean sameClasses(Class[] params, Object arg1, Object arg2, Object arg3) {
      if (params.length != 3) {
         return false;
      } else if (arg1 != null && (params[0] == arg1.getClass() || arg1 instanceof Wrapper && params[0] == ((Wrapper)arg1).getType())) {
         if (arg2 == null || params[1] != arg2.getClass() && (!(arg2 instanceof Wrapper) || params[1] != ((Wrapper)arg2).getType())) {
            return false;
         } else {
            return arg3 != null && (params[2] == arg3.getClass() || arg3 instanceof Wrapper && params[2] == ((Wrapper)arg3).getType());
         }
      } else {
         return false;
      }
   }

   public static boolean sameClasses(Class[] params, Object arg1, Object arg2, Object arg3, Object arg4) {
      if (params.length != 4) {
         return false;
      } else if (arg1 != null && (params[0] == arg1.getClass() || arg1 instanceof Wrapper && params[0] == ((Wrapper)arg1).getType())) {
         if (arg2 != null && (params[1] == arg2.getClass() || arg2 instanceof Wrapper && params[1] == ((Wrapper)arg2).getType())) {
            if (arg3 != null && (params[2] == arg3.getClass() || arg3 instanceof Wrapper && params[2] == ((Wrapper)arg3).getType())) {
               return arg4 != null && (params[3] == arg4.getClass() || arg4 instanceof Wrapper && params[3] == ((Wrapper)arg4).getType());
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean sameClass(Class[] params, Object arg) {
      return arg != null && (params[0] == arg.getClass() || arg instanceof Wrapper && params[0] == ((Wrapper)arg).getType());
   }

   public static Class[] castArgumentsToClassArray(Object[] argTypes) {
      if (argTypes == null) {
         return EMPTY_CLASS_ARRAY;
      } else {
         Class[] classes = new Class[argTypes.length];

         for(int i = 0; i < argTypes.length; ++i) {
            Object argType = argTypes[i];
            if (argType instanceof Class) {
               classes[i] = (Class)argType;
            } else if (argType == null) {
               classes[i] = null;
            } else {
               classes[i] = argType.getClass();
            }
         }

         return classes;
      }
   }

   public static void unwrap(Object[] arguments) {
      for(int i = 0; i != arguments.length; ++i) {
         if (arguments[i] instanceof Wrapper) {
            arguments[i] = ((Wrapper)arguments[i]).unwrap();
         }
      }

   }

   static {
      PRIMITIVES = new Class[]{Byte.TYPE, Byte.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, BigInteger.class, Float.TYPE, Float.class, Double.TYPE, Double.class, BigDecimal.class, Number.class, Object.class};
      PRIMITIVE_DISTANCE_TABLE = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {14, 15, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, {14, 15, 1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, {14, 15, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, {14, 15, 12, 13, 1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, {14, 15, 12, 13, 10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {14, 15, 12, 13, 10, 11, 1, 0, 2, 3, 4, 5, 6, 7, 8, 9}, {9, 10, 7, 8, 5, 6, 3, 4, 0, 14, 15, 12, 13, 11, 1, 2}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 0, 1, 2, 3, 4, 5, 6}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 1, 0, 2, 3, 4, 5, 6}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 5, 6, 0, 1, 2, 3, 4}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 5, 6, 1, 0, 2, 3, 4}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 5, 6, 3, 4, 0, 1, 2}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 5, 6, 3, 4, 2, 0, 1}, {14, 15, 12, 13, 10, 11, 8, 9, 7, 5, 6, 3, 4, 2, 1, 0}};
   }
}
