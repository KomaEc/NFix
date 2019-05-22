package org.codehaus.groovy.runtime.typehandling;

import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.IteratorClosureAdapter;
import org.codehaus.groovy.runtime.MethodClosure;

public class DefaultTypeTransformation {
   protected static final Object[] EMPTY_ARGUMENTS = new Object[0];
   protected static final BigInteger ONE_NEG = new BigInteger("-1");

   public static byte byteUnbox(Object value) {
      Number n = castToNumber(value);
      return n.byteValue();
   }

   public static char charUnbox(Object value) {
      return castToChar(value);
   }

   public static short shortUnbox(Object value) {
      Number n = castToNumber(value);
      return n.shortValue();
   }

   public static int intUnbox(Object value) {
      Number n = castToNumber(value);
      return n.intValue();
   }

   public static boolean booleanUnbox(Object value) {
      return castToBoolean(value);
   }

   public static long longUnbox(Object value) {
      Number n = castToNumber(value);
      return n.longValue();
   }

   public static float floatUnbox(Object value) {
      Number n = castToNumber(value);
      return n.floatValue();
   }

   public static double doubleUnbox(Object value) {
      Number n = castToNumber(value);
      return n.doubleValue();
   }

   public static Object box(boolean value) {
      return value ? Boolean.TRUE : Boolean.FALSE;
   }

   public static Object box(byte value) {
      return value;
   }

   public static Object box(char value) {
      return value;
   }

   public static Object box(short value) {
      return value;
   }

   public static Object box(int value) {
      return value;
   }

   public static Object box(long value) {
      return value;
   }

   public static Object box(float value) {
      return value;
   }

   public static Object box(double value) {
      return value;
   }

   public static Number castToNumber(Object object) {
      return castToNumber(object, Number.class);
   }

   public static Number castToNumber(Object object, Class type) {
      if (object instanceof Number) {
         return (Number)object;
      } else if (object instanceof Character) {
         return Integer.valueOf((Character)object);
      } else if (object instanceof String) {
         String c = (String)object;
         if (c.length() == 1) {
            return Integer.valueOf(c.charAt(0));
         } else {
            throw new GroovyCastException(c, type);
         }
      } else {
         throw new GroovyCastException(object, type);
      }
   }

   public static boolean castToBoolean(Object object) {
      return object == null ? false : (Boolean)InvokerHelper.invokeMethod(object, "asBoolean", InvokerHelper.EMPTY_ARGS);
   }

   public static char castToChar(Object object) {
      if (object instanceof Character) {
         return (Character)object;
      } else if (object instanceof Number) {
         Number value = (Number)object;
         return (char)value.intValue();
      } else {
         String text = object.toString();
         if (text.length() == 1) {
            return text.charAt(0);
         } else {
            throw new GroovyCastException(text, Character.TYPE);
         }
      }
   }

   public static Object castToType(Object object, Class type) {
      if (object == null) {
         return null;
      } else if (type == Object.class) {
         return object;
      } else {
         Class aClass = object.getClass();
         if (type == aClass) {
            return object;
         } else if (ReflectionCache.isArray(type)) {
            return asArray(object, type);
         } else if (ReflectionCache.isAssignableFrom(type, aClass)) {
            return object;
         } else {
            if (Collection.class.isAssignableFrom(type)) {
               int modifiers = type.getModifiers();
               if (object instanceof Collection && type.isAssignableFrom(HashSet.class) && (type == HashSet.class || Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))) {
                  return new HashSet((Collection)object);
               }

               if (aClass.isArray()) {
                  Object answer;
                  if (!type.isAssignableFrom(ArrayList.class) || !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                     try {
                        answer = (Collection)type.newInstance();
                     } catch (Exception var8) {
                        throw new GroovyCastException("Could not instantiate instance of: " + type.getName() + ". Reason: " + var8);
                     }
                  } else {
                     answer = new ArrayList();
                  }

                  int length = Array.getLength(object);

                  for(int i = 0; i < length; ++i) {
                     Object element = Array.get(object, i);
                     ((Collection)answer).add(element);
                  }

                  return answer;
               }
            }

            if (type == String.class) {
               return object.toString();
            } else if (type == Character.class) {
               return box(castToChar(object));
            } else if (type == Boolean.class) {
               return box(castToBoolean(object));
            } else if (type == Class.class) {
               return castToClass(object);
            } else {
               if (Number.class.isAssignableFrom(type)) {
                  Number n = castToNumber(object, type);
                  if (type == Byte.class) {
                     return new Byte(n.byteValue());
                  }

                  if (type == Character.class) {
                     return new Character((char)n.intValue());
                  }

                  if (type == Short.class) {
                     return new Short(n.shortValue());
                  }

                  if (type == Integer.class) {
                     return n.intValue();
                  }

                  if (type == Long.class) {
                     return new Long(n.longValue());
                  }

                  if (type == Float.class) {
                     return new Float(n.floatValue());
                  }

                  if (type == Double.class) {
                     Double answer = new Double(n.doubleValue());
                     if (n instanceof Double || answer != Double.NEGATIVE_INFINITY && answer != Double.POSITIVE_INFINITY) {
                        return answer;
                     }

                     throw new GroovyRuntimeException("Automatic coercion of " + n.getClass().getName() + " value " + n + " to double failed.  Value is out of range.");
                  }

                  if (type == BigDecimal.class) {
                     if (!(n instanceof Float) && !(n instanceof Double)) {
                        return new BigDecimal(n.toString());
                     }

                     return new BigDecimal(n.doubleValue());
                  }

                  if (type == BigInteger.class) {
                     if (!(object instanceof Float) && !(object instanceof Double)) {
                        if (object instanceof BigDecimal) {
                           return ((BigDecimal)object).toBigInteger();
                        }

                        return new BigInteger(n.toString());
                     }

                     BigDecimal bd = new BigDecimal(n.doubleValue());
                     return bd.toBigInteger();
                  }
               } else if (type.isPrimitive()) {
                  if (type == Boolean.TYPE) {
                     return box(booleanUnbox(object));
                  }

                  if (type == Byte.TYPE) {
                     return box(byteUnbox(object));
                  }

                  if (type == Character.TYPE) {
                     return box(charUnbox(object));
                  }

                  if (type == Short.TYPE) {
                     return box(shortUnbox(object));
                  }

                  if (type == Integer.TYPE) {
                     return box(intUnbox(object));
                  }

                  if (type == Long.TYPE) {
                     return box(longUnbox(object));
                  }

                  if (type == Float.TYPE) {
                     return box(floatUnbox(object));
                  }

                  if (type == Double.TYPE) {
                     Double answer = new Double(doubleUnbox(object));
                     if (object instanceof Double || answer != Double.NEGATIVE_INFINITY && answer != Double.POSITIVE_INFINITY) {
                        return answer;
                     }

                     throw new GroovyRuntimeException("Automatic coercion of " + aClass.getName() + " value " + object + " to double failed.  Value is out of range.");
                  }
               } else {
                  if (object instanceof String && type.isEnum()) {
                     return Enum.valueOf(type, (String)object);
                  }

                  if (object instanceof GString && type.isEnum()) {
                     return Enum.valueOf(type, object.toString());
                  }
               }

               Object[] args = null;
               if (object instanceof Collection) {
                  Collection collection = (Collection)object;
                  args = collection.toArray();
               } else if (object instanceof Object[]) {
                  args = (Object[])((Object[])object);
               } else if (object instanceof Map) {
                  args = new Object[]{object};
               }

               Exception nested = null;
               if (args != null) {
                  try {
                     return InvokerHelper.invokeConstructorOf((Class)type, args);
                  } catch (InvokerInvocationException var9) {
                     throw var9;
                  } catch (Exception var10) {
                     nested = var10;
                  }
               }

               GroovyCastException gce;
               if (nested != null) {
                  gce = new GroovyCastException(object, type, nested);
               } else {
                  gce = new GroovyCastException(object, type);
               }

               throw gce;
            }
         }
      }
   }

   private static Class castToClass(Object object) {
      try {
         return Class.forName(object.toString());
      } catch (Exception var2) {
         throw new GroovyCastException(object, Class.class, var2);
      }
   }

   public static Object asArray(Object object, Class type) {
      if (type.isAssignableFrom(object.getClass())) {
         return object;
      } else {
         Collection list = asCollection(object);
         int size = list.size();
         Class elementType = type.getComponentType();
         Object array = Array.newInstance(elementType, size);
         int idx = 0;
         Iterator iter;
         Object element;
         if (Boolean.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setBoolean(array, idx, booleanUnbox(element));
            }
         } else if (Byte.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setByte(array, idx, byteUnbox(element));
            }
         } else if (Character.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setChar(array, idx, charUnbox(element));
            }
         } else if (Double.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setDouble(array, idx, doubleUnbox(element));
            }
         } else if (Float.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setFloat(array, idx, floatUnbox(element));
            }
         } else if (Integer.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setInt(array, idx, intUnbox(element));
            }
         } else if (Long.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setLong(array, idx, longUnbox(element));
            }
         } else if (Short.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setShort(array, idx, shortUnbox(element));
            }
         } else {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Object coercedElement = castToType(element, elementType);
               Array.set(array, idx, coercedElement);
            }
         }

         return array;
      }
   }

   public static <T> Collection<T> asCollection(T[] value) {
      return arrayAsCollection(value);
   }

   public static Collection asCollection(Object value) {
      if (value == null) {
         return Collections.EMPTY_LIST;
      } else if (value instanceof Collection) {
         return (Collection)value;
      } else if (value instanceof Map) {
         Map map = (Map)value;
         return map.entrySet();
      } else if (value.getClass().isArray()) {
         return arrayAsCollection(value);
      } else if (value instanceof MethodClosure) {
         MethodClosure method = (MethodClosure)value;
         IteratorClosureAdapter adapter = new IteratorClosureAdapter(method.getDelegate());
         method.call(adapter);
         return adapter.asList();
      } else if (value instanceof String) {
         return DefaultGroovyMethods.toList((String)value);
      } else if (value instanceof GString) {
         return DefaultGroovyMethods.toList(value.toString());
      } else if (value instanceof File) {
         try {
            return DefaultGroovyMethods.readLines((File)value);
         } catch (IOException var3) {
            throw new GroovyRuntimeException("Error reading file: " + value, var3);
         }
      } else if (isEnumSubclass(value)) {
         Object[] values = (Object[])((Object[])InvokerHelper.invokeMethod(value, "values", new Object[0]));
         return Arrays.asList(values);
      } else {
         return Collections.singletonList(value);
      }
   }

   public static Collection arrayAsCollection(Object value) {
      return (Collection)(value.getClass().getComponentType().isPrimitive() ? primitiveArrayToList(value) : arrayAsCollection((Object[])((Object[])value)));
   }

   public static <T> Collection<T> arrayAsCollection(T[] value) {
      return Arrays.asList((Object[])value);
   }

   public static boolean isEnumSubclass(Object value) {
      if (value instanceof Class) {
         for(Class superclass = ((Class)value).getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            if (superclass.getName().equals("java.lang.Enum")) {
               return true;
            }
         }
      }

      return false;
   }

   public static List primitiveArrayToList(Object array) {
      int size = Array.getLength(array);
      List list = new ArrayList(size);

      for(int i = 0; i < size; ++i) {
         Object item = Array.get(array, i);
         if (item != null && item.getClass().isArray() && item.getClass().getComponentType().isPrimitive()) {
            item = primitiveArrayToList(item);
         }

         list.add(item);
      }

      return list;
   }

   public static Object[] primitiveArrayBox(Object array) {
      int size = Array.getLength(array);
      Object[] ret = (Object[])((Object[])Array.newInstance(ReflectionCache.autoboxType(array.getClass().getComponentType()), size));

      for(int i = 0; i < size; ++i) {
         ret[i] = Array.get(array, i);
      }

      return ret;
   }

   public static int compareTo(Object left, Object right) {
      return compareToWithEqualityCheck(left, right, false);
   }

   private static int compareToWithEqualityCheck(Object left, Object right, boolean equalityCheckOnly) {
      if (left == right) {
         return 0;
      } else if (left == null) {
         return -1;
      } else if (right == null) {
         return 1;
      } else {
         if (left instanceof Comparable) {
            if (left instanceof Number) {
               if (isValidCharacterString(right)) {
                  return DefaultGroovyMethods.compareTo((Number)left, (Character)box(castToChar(right)));
               }

               if (right instanceof Character || right instanceof Number) {
                  return DefaultGroovyMethods.compareTo((Number)left, castToNumber(right));
               }
            } else if (left instanceof Character) {
               if (isValidCharacterString(right)) {
                  return DefaultGroovyMethods.compareTo((Character)left, (Character)box(castToChar(right)));
               }

               if (right instanceof Number) {
                  return DefaultGroovyMethods.compareTo((Character)left, (Number)right);
               }
            } else if (right instanceof Number) {
               if (isValidCharacterString(left)) {
                  return DefaultGroovyMethods.compareTo((Character)box(castToChar(left)), (Number)right);
               }
            } else {
               if (left instanceof String && right instanceof Character) {
                  return ((String)left).compareTo(right.toString());
               }

               if (left instanceof String && right instanceof GString) {
                  return ((String)left).compareTo(right.toString());
               }
            }

            if (!equalityCheckOnly || left.getClass().isAssignableFrom(right.getClass()) || right.getClass() != Object.class && right.getClass().isAssignableFrom(left.getClass()) || left instanceof GString && right instanceof String) {
               Comparable comparable = (Comparable)left;
               return comparable.compareTo(right);
            }
         }

         if (equalityCheckOnly) {
            return -1;
         } else {
            throw new GroovyRuntimeException("Cannot compare " + left.getClass().getName() + " with value '" + left + "' and " + right.getClass().getName() + " with value '" + right + "'");
         }
      }
   }

   public static boolean compareEqual(Object left, Object right) {
      if (left == right) {
         return true;
      } else if (left != null && right != null) {
         if (left instanceof Comparable) {
            return compareToWithEqualityCheck(left, right, true) == 0;
         } else {
            Class leftClass = left.getClass();
            Class rightClass = right.getClass();
            if (leftClass.isArray() && rightClass.isArray()) {
               return compareArrayEqual(left, right);
            } else {
               if (leftClass.isArray() && leftClass.getComponentType().isPrimitive()) {
                  left = primitiveArrayToList(left);
               }

               if (rightClass.isArray() && rightClass.getComponentType().isPrimitive()) {
                  right = primitiveArrayToList(right);
               }

               if (left instanceof Object[] && right instanceof List) {
                  return DefaultGroovyMethods.equals((Object[])((Object[])left), (List)right);
               } else if (left instanceof List && right instanceof Object[]) {
                  return DefaultGroovyMethods.equals((List)left, (Object[])((Object[])right));
               } else if (left instanceof List && right instanceof List) {
                  return DefaultGroovyMethods.equals((List)left, (List)right);
               } else if (left instanceof Entry && right instanceof Entry) {
                  Object k1 = ((Entry)left).getKey();
                  Object k2 = ((Entry)right).getKey();
                  if (k1 == k2 || k1 != null && k1.equals(k2)) {
                     Object v1 = ((Entry)left).getValue();
                     Object v2 = ((Entry)right).getValue();
                     if (v1 == v2 || v1 != null && compareEqual(v1, v2)) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return (Boolean)InvokerHelper.invokeMethod(left, "equals", right);
               }
            }
         }
      } else {
         return false;
      }
   }

   public static boolean compareArrayEqual(Object left, Object right) {
      if (left == null) {
         return right == null;
      } else if (right == null) {
         return false;
      } else if (Array.getLength(left) != Array.getLength(right)) {
         return false;
      } else {
         for(int i = 0; i < Array.getLength(left); ++i) {
            Object l = Array.get(left, i);
            Object r = Array.get(right, i);
            if (!compareEqual(l, r)) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean isValidCharacterString(Object value) {
      if (value instanceof String) {
         String s = (String)value;
         if (s.length() == 1) {
            return true;
         }
      }

      return false;
   }

   public static int[] convertToIntArray(Object a) {
      int[] ans = null;
      int[] ans;
      if (a.getClass().getName().equals("[I")) {
         ans = (int[])((int[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new int[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = ((Number)ia[i]).intValue();
            }
         }
      }

      return ans;
   }

   public static boolean[] convertToBooleanArray(Object a) {
      boolean[] ans = null;
      boolean[] ans;
      if (a instanceof boolean[]) {
         ans = (boolean[])((boolean[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new boolean[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = (Boolean)ia[i];
            }
         }
      }

      return ans;
   }

   public static byte[] convertToByteArray(Object a) {
      byte[] ans = null;
      byte[] ans;
      if (a instanceof byte[]) {
         ans = (byte[])((byte[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new byte[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = ((Number)ia[i]).byteValue();
            }
         }
      }

      return ans;
   }

   public static short[] convertToShortArray(Object a) {
      short[] ans = null;
      short[] ans;
      if (a instanceof short[]) {
         ans = (short[])((short[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new short[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            ans[i] = ((Number)ia[i]).shortValue();
         }
      }

      return ans;
   }

   public static char[] convertToCharArray(Object a) {
      char[] ans = null;
      char[] ans;
      if (a instanceof char[]) {
         ans = (char[])((char[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new char[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = (Character)ia[i];
            }
         }
      }

      return ans;
   }

   public static long[] convertToLongArray(Object a) {
      long[] ans = null;
      long[] ans;
      if (a instanceof long[]) {
         ans = (long[])((long[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new long[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = ((Number)ia[i]).longValue();
            }
         }
      }

      return ans;
   }

   public static float[] convertToFloatArray(Object a) {
      float[] ans = null;
      float[] ans;
      if (a instanceof float[]) {
         ans = (float[])((float[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new float[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = ((Number)ia[i]).floatValue();
            }
         }
      }

      return ans;
   }

   public static double[] convertToDoubleArray(Object a) {
      double[] ans = null;
      double[] ans;
      if (a instanceof double[]) {
         ans = (double[])((double[])a);
      } else {
         Object[] ia = (Object[])((Object[])a);
         ans = new double[ia.length];

         for(int i = 0; i < ia.length; ++i) {
            if (ia[i] != null) {
               ans[i] = ((Number)ia[i]).doubleValue();
            }
         }
      }

      return ans;
   }

   public static Object convertToPrimitiveArray(Object a, Class type) {
      if (type == Byte.TYPE) {
         return convertToByteArray(a);
      } else if (type == Boolean.TYPE) {
         return convertToBooleanArray(a);
      } else if (type == Short.TYPE) {
         return convertToShortArray(a);
      } else if (type == Character.TYPE) {
         return convertToCharArray(a);
      } else if (type == Integer.TYPE) {
         return convertToIntArray(a);
      } else if (type == Long.TYPE) {
         return convertToLongArray(a);
      } else if (type == Float.TYPE) {
         return convertToFloatArray(a);
      } else {
         return type == Double.TYPE ? convertToDoubleArray(a) : a;
      }
   }

   public static Character getCharFromSizeOneString(Object value) {
      if (value instanceof GString) {
         value = value.toString();
      }

      if (value instanceof String) {
         String s = (String)value;
         if (s.length() != 1) {
            throw new IllegalArgumentException("String of length 1 expected but got a bigger one");
         } else {
            return new Character(s.charAt(0));
         }
      } else {
         return (Character)value;
      }
   }
}
