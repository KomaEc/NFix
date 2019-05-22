package org.codehaus.groovy.runtime.typehandling;

import groovy.lang.MetaMethod;
import java.util.Collections;
import java.util.HashSet;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;

public class NumberMathModificationInfo {
   public static final NumberMathModificationInfo instance = new NumberMathModificationInfo();
   private final HashSet<String> names = new HashSet();
   public boolean byte_plus;
   public boolean short_plus;
   public boolean int_plus;
   public boolean long_plus;
   public boolean float_plus;
   public boolean double_plus;
   public boolean byte_minus;
   public boolean short_minus;
   public boolean int_minus;
   public boolean long_minus;
   public boolean float_minus;
   public boolean double_minus;
   public boolean byte_multiply;
   public boolean short_multiply;
   public boolean int_multiply;
   public boolean long_multiply;
   public boolean float_multiply;
   public boolean double_multiply;
   public boolean byte_div;
   public boolean short_div;
   public boolean int_div;
   public boolean long_div;
   public boolean float_div;
   public boolean double_div;
   public boolean byte_or;
   public boolean short_or;
   public boolean int_or;
   public boolean long_or;
   public boolean float_or;
   public boolean double_or;
   public boolean byte_and;
   public boolean short_and;
   public boolean int_and;
   public boolean long_and;
   public boolean float_and;
   public boolean double_and;
   public boolean byte_xor;
   public boolean short_xor;
   public boolean int_xor;
   public boolean long_xor;
   public boolean float_xor;
   public boolean double_xor;
   public boolean byte_intdiv;
   public boolean short_intdiv;
   public boolean int_intdiv;
   public boolean long_intdiv;
   public boolean float_intdiv;
   public boolean double_intdiv;
   public boolean byte_mod;
   public boolean short_mod;
   public boolean int_mod;
   public boolean long_mod;
   public boolean float_mod;
   public boolean double_mod;
   public boolean byte_leftShift;
   public boolean short_leftShift;
   public boolean int_leftShift;
   public boolean long_leftShift;
   public boolean float_leftShift;
   public boolean double_leftShift;
   public boolean byte_rightShift;
   public boolean short_rightShift;
   public boolean int_rightShift;
   public boolean long_rightShift;
   public boolean float_rightShift;
   public boolean double_rightShift;
   public boolean byte_rightShiftUnsigned;
   public boolean short_rightShiftUnsigned;
   public boolean int_rightShiftUnsigned;
   public boolean long_rightShiftUnsigned;
   public boolean float_rightShiftUnsigned;
   public boolean double_rightShiftUnsigned;

   private NumberMathModificationInfo() {
      Collections.addAll(this.names, new String[]{"plus", "minus", "multiply", "div", "compareTo", "or", "and", "xor", "intdiv", "mod", "leftShift", "rightShift", "rightShiftUnsigned"});
   }

   public void checkIfStdMethod(MetaMethod method) {
      if (method.getClass() != NewInstanceMetaMethod.class) {
         String name = method.getName();
         if (method.getParameterTypes().length != 1) {
            return;
         }

         if (!method.getParameterTypes()[0].isNumber && method.getParameterTypes()[0].getTheClass() != Object.class) {
            return;
         }

         if (!this.names.contains(name)) {
            return;
         }

         this.checkNumberOps(name, method.getDeclaringClass().getTheClass());
      }

   }

   private void checkNumberOps(String name, Class klazz) {
      if ("plus".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_plus = true;
         }

         if (klazz == Short.class) {
            this.short_plus = true;
         }

         if (klazz == Integer.class) {
            this.int_plus = true;
         }

         if (klazz == Long.class) {
            this.long_plus = true;
         }

         if (klazz == Float.class) {
            this.float_plus = true;
         }

         if (klazz == Double.class) {
            this.double_plus = true;
         }

         if (klazz == Object.class) {
            this.byte_plus = true;
            this.short_plus = true;
            this.int_plus = true;
            this.long_plus = true;
            this.float_plus = true;
            this.double_plus = true;
         }
      }

      if ("minus".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_minus = true;
         }

         if (klazz == Short.class) {
            this.short_minus = true;
         }

         if (klazz == Integer.class) {
            this.int_minus = true;
         }

         if (klazz == Long.class) {
            this.long_minus = true;
         }

         if (klazz == Float.class) {
            this.float_minus = true;
         }

         if (klazz == Double.class) {
            this.double_minus = true;
         }

         if (klazz == Object.class) {
            this.byte_minus = true;
            this.short_minus = true;
            this.int_minus = true;
            this.long_minus = true;
            this.float_minus = true;
            this.double_minus = true;
         }
      }

      if ("multiply".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_multiply = true;
         }

         if (klazz == Short.class) {
            this.short_multiply = true;
         }

         if (klazz == Integer.class) {
            this.int_multiply = true;
         }

         if (klazz == Long.class) {
            this.long_multiply = true;
         }

         if (klazz == Float.class) {
            this.float_multiply = true;
         }

         if (klazz == Double.class) {
            this.double_multiply = true;
         }

         if (klazz == Object.class) {
            this.byte_multiply = true;
            this.short_multiply = true;
            this.int_multiply = true;
            this.long_multiply = true;
            this.float_multiply = true;
            this.double_multiply = true;
         }
      }

      if ("div".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_div = true;
         }

         if (klazz == Short.class) {
            this.short_div = true;
         }

         if (klazz == Integer.class) {
            this.int_div = true;
         }

         if (klazz == Long.class) {
            this.long_div = true;
         }

         if (klazz == Float.class) {
            this.float_div = true;
         }

         if (klazz == Double.class) {
            this.double_div = true;
         }

         if (klazz == Object.class) {
            this.byte_div = true;
            this.short_div = true;
            this.int_div = true;
            this.long_div = true;
            this.float_div = true;
            this.double_div = true;
         }
      }

      if ("or".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_or = true;
         }

         if (klazz == Short.class) {
            this.short_or = true;
         }

         if (klazz == Integer.class) {
            this.int_or = true;
         }

         if (klazz == Long.class) {
            this.long_or = true;
         }

         if (klazz == Float.class) {
            this.float_or = true;
         }

         if (klazz == Double.class) {
            this.double_or = true;
         }

         if (klazz == Object.class) {
            this.byte_or = true;
            this.short_or = true;
            this.int_or = true;
            this.long_or = true;
            this.float_or = true;
            this.double_or = true;
         }
      }

      if ("and".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_and = true;
         }

         if (klazz == Short.class) {
            this.short_and = true;
         }

         if (klazz == Integer.class) {
            this.int_and = true;
         }

         if (klazz == Long.class) {
            this.long_and = true;
         }

         if (klazz == Float.class) {
            this.float_and = true;
         }

         if (klazz == Double.class) {
            this.double_and = true;
         }

         if (klazz == Object.class) {
            this.byte_and = true;
            this.short_and = true;
            this.int_and = true;
            this.long_and = true;
            this.float_and = true;
            this.double_and = true;
         }
      }

      if ("xor".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_xor = true;
         }

         if (klazz == Short.class) {
            this.short_xor = true;
         }

         if (klazz == Integer.class) {
            this.int_xor = true;
         }

         if (klazz == Long.class) {
            this.long_xor = true;
         }

         if (klazz == Float.class) {
            this.float_xor = true;
         }

         if (klazz == Double.class) {
            this.double_xor = true;
         }

         if (klazz == Object.class) {
            this.byte_xor = true;
            this.short_xor = true;
            this.int_xor = true;
            this.long_xor = true;
            this.float_xor = true;
            this.double_xor = true;
         }
      }

      if ("intdiv".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_intdiv = true;
         }

         if (klazz == Short.class) {
            this.short_intdiv = true;
         }

         if (klazz == Integer.class) {
            this.int_intdiv = true;
         }

         if (klazz == Long.class) {
            this.long_intdiv = true;
         }

         if (klazz == Float.class) {
            this.float_intdiv = true;
         }

         if (klazz == Double.class) {
            this.double_intdiv = true;
         }

         if (klazz == Object.class) {
            this.byte_intdiv = true;
            this.short_intdiv = true;
            this.int_intdiv = true;
            this.long_intdiv = true;
            this.float_intdiv = true;
            this.double_intdiv = true;
         }
      }

      if ("mod".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_mod = true;
         }

         if (klazz == Short.class) {
            this.short_mod = true;
         }

         if (klazz == Integer.class) {
            this.int_mod = true;
         }

         if (klazz == Long.class) {
            this.long_mod = true;
         }

         if (klazz == Float.class) {
            this.float_mod = true;
         }

         if (klazz == Double.class) {
            this.double_mod = true;
         }

         if (klazz == Object.class) {
            this.byte_mod = true;
            this.short_mod = true;
            this.int_mod = true;
            this.long_mod = true;
            this.float_mod = true;
            this.double_mod = true;
         }
      }

      if ("leftShift".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_leftShift = true;
         }

         if (klazz == Short.class) {
            this.short_leftShift = true;
         }

         if (klazz == Integer.class) {
            this.int_leftShift = true;
         }

         if (klazz == Long.class) {
            this.long_leftShift = true;
         }

         if (klazz == Float.class) {
            this.float_leftShift = true;
         }

         if (klazz == Double.class) {
            this.double_leftShift = true;
         }

         if (klazz == Object.class) {
            this.byte_leftShift = true;
            this.short_leftShift = true;
            this.int_leftShift = true;
            this.long_leftShift = true;
            this.float_leftShift = true;
            this.double_leftShift = true;
         }
      }

      if ("rightShift".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_rightShift = true;
         }

         if (klazz == Short.class) {
            this.short_rightShift = true;
         }

         if (klazz == Integer.class) {
            this.int_rightShift = true;
         }

         if (klazz == Long.class) {
            this.long_rightShift = true;
         }

         if (klazz == Float.class) {
            this.float_rightShift = true;
         }

         if (klazz == Double.class) {
            this.double_rightShift = true;
         }

         if (klazz == Object.class) {
            this.byte_rightShift = true;
            this.short_rightShift = true;
            this.int_rightShift = true;
            this.long_rightShift = true;
            this.float_rightShift = true;
            this.double_rightShift = true;
         }
      }

      if ("rightShiftUnsigned".equals(name)) {
         if (klazz == Byte.class) {
            this.byte_rightShiftUnsigned = true;
         }

         if (klazz == Short.class) {
            this.short_rightShiftUnsigned = true;
         }

         if (klazz == Integer.class) {
            this.int_rightShiftUnsigned = true;
         }

         if (klazz == Long.class) {
            this.long_rightShiftUnsigned = true;
         }

         if (klazz == Float.class) {
            this.float_rightShiftUnsigned = true;
         }

         if (klazz == Double.class) {
            this.double_rightShiftUnsigned = true;
         }

         if (klazz == Object.class) {
            this.byte_rightShiftUnsigned = true;
            this.short_rightShiftUnsigned = true;
            this.int_rightShiftUnsigned = true;
            this.long_rightShiftUnsigned = true;
            this.float_rightShiftUnsigned = true;
            this.double_rightShiftUnsigned = true;
         }
      }

   }

   public static int plus(byte op1, byte op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(byte op1, short op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(byte op1, int op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static long plus(byte op1, long op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : (long)op1 + op2;
   }

   private static long plusSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static double plus(byte op1, float op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(byte op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(byte op1, double op2) {
      return instance.byte_plus ? plusSlow(op1, op2) : (double)op1 + op2;
   }

   private static double plusSlow(byte op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static int plus(short op1, byte op2) {
      return instance.short_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(short op1, short op2) {
      return instance.short_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(short op1, int op2) {
      return instance.short_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static long plus(short op1, long op2) {
      return instance.short_plus ? plusSlow(op1, op2) : (long)op1 + op2;
   }

   private static long plusSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static double plus(short op1, float op2) {
      return instance.short_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(short op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(short op1, double op2) {
      return instance.short_plus ? plusSlow(op1, op2) : (double)op1 + op2;
   }

   private static double plusSlow(short op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static int plus(int op1, byte op2) {
      return instance.int_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(int op1, short op2) {
      return instance.int_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static int plus(int op1, int op2) {
      return instance.int_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static int plusSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).intValue();
   }

   public static long plus(int op1, long op2) {
      return instance.int_plus ? plusSlow(op1, op2) : (long)op1 + op2;
   }

   private static long plusSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static double plus(int op1, float op2) {
      return instance.int_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(int op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(int op1, double op2) {
      return instance.int_plus ? plusSlow(op1, op2) : (double)op1 + op2;
   }

   private static double plusSlow(int op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static long plus(long op1, byte op2) {
      return instance.long_plus ? plusSlow(op1, op2) : op1 + (long)op2;
   }

   private static long plusSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static long plus(long op1, short op2) {
      return instance.long_plus ? plusSlow(op1, op2) : op1 + (long)op2;
   }

   private static long plusSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static long plus(long op1, int op2) {
      return instance.long_plus ? plusSlow(op1, op2) : op1 + (long)op2;
   }

   private static long plusSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static long plus(long op1, long op2) {
      return instance.long_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static long plusSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).longValue();
   }

   public static double plus(long op1, float op2) {
      return instance.long_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(long op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(long op1, double op2) {
      return instance.long_plus ? plusSlow(op1, op2) : (double)op1 + op2;
   }

   private static double plusSlow(long op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, byte op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(float op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, short op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(float op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, int op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(float op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, long op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(float op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, float op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + (double)op2;
   }

   private static double plusSlow(float op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(float op1, double op2) {
      return instance.float_plus ? plusSlow(op1, op2) : (double)op1 + op2;
   }

   private static double plusSlow(float op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, byte op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + (double)op2;
   }

   private static double plusSlow(double op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, short op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + (double)op2;
   }

   private static double plusSlow(double op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, int op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + (double)op2;
   }

   private static double plusSlow(double op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, long op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + (double)op2;
   }

   private static double plusSlow(double op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, float op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + (double)op2;
   }

   private static double plusSlow(double op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static double plus(double op1, double op2) {
      return instance.double_plus ? plusSlow(op1, op2) : op1 + op2;
   }

   private static double plusSlow(double op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "plus", op2)).doubleValue();
   }

   public static int minus(byte op1, byte op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(byte op1, short op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(byte op1, int op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static long minus(byte op1, long op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : (long)op1 - op2;
   }

   private static long minusSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static double minus(byte op1, float op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(byte op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(byte op1, double op2) {
      return instance.byte_minus ? minusSlow(op1, op2) : (double)op1 - op2;
   }

   private static double minusSlow(byte op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static int minus(short op1, byte op2) {
      return instance.short_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(short op1, short op2) {
      return instance.short_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(short op1, int op2) {
      return instance.short_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static long minus(short op1, long op2) {
      return instance.short_minus ? minusSlow(op1, op2) : (long)op1 - op2;
   }

   private static long minusSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static double minus(short op1, float op2) {
      return instance.short_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(short op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(short op1, double op2) {
      return instance.short_minus ? minusSlow(op1, op2) : (double)op1 - op2;
   }

   private static double minusSlow(short op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static int minus(int op1, byte op2) {
      return instance.int_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(int op1, short op2) {
      return instance.int_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static int minus(int op1, int op2) {
      return instance.int_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static int minusSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).intValue();
   }

   public static long minus(int op1, long op2) {
      return instance.int_minus ? minusSlow(op1, op2) : (long)op1 - op2;
   }

   private static long minusSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static double minus(int op1, float op2) {
      return instance.int_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(int op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(int op1, double op2) {
      return instance.int_minus ? minusSlow(op1, op2) : (double)op1 - op2;
   }

   private static double minusSlow(int op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static long minus(long op1, byte op2) {
      return instance.long_minus ? minusSlow(op1, op2) : op1 - (long)op2;
   }

   private static long minusSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static long minus(long op1, short op2) {
      return instance.long_minus ? minusSlow(op1, op2) : op1 - (long)op2;
   }

   private static long minusSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static long minus(long op1, int op2) {
      return instance.long_minus ? minusSlow(op1, op2) : op1 - (long)op2;
   }

   private static long minusSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static long minus(long op1, long op2) {
      return instance.long_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static long minusSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).longValue();
   }

   public static double minus(long op1, float op2) {
      return instance.long_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(long op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(long op1, double op2) {
      return instance.long_minus ? minusSlow(op1, op2) : (double)op1 - op2;
   }

   private static double minusSlow(long op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, byte op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(float op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, short op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(float op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, int op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(float op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, long op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(float op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, float op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - (double)op2;
   }

   private static double minusSlow(float op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(float op1, double op2) {
      return instance.float_minus ? minusSlow(op1, op2) : (double)op1 - op2;
   }

   private static double minusSlow(float op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, byte op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - (double)op2;
   }

   private static double minusSlow(double op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, short op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - (double)op2;
   }

   private static double minusSlow(double op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, int op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - (double)op2;
   }

   private static double minusSlow(double op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, long op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - (double)op2;
   }

   private static double minusSlow(double op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, float op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - (double)op2;
   }

   private static double minusSlow(double op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static double minus(double op1, double op2) {
      return instance.double_minus ? minusSlow(op1, op2) : op1 - op2;
   }

   private static double minusSlow(double op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "minus", op2)).doubleValue();
   }

   public static int multiply(byte op1, byte op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(byte op1, short op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(byte op1, int op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static long multiply(byte op1, long op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : (long)op1 * op2;
   }

   private static long multiplySlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static double multiply(byte op1, float op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(byte op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(byte op1, double op2) {
      return instance.byte_multiply ? multiplySlow(op1, op2) : (double)op1 * op2;
   }

   private static double multiplySlow(byte op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static int multiply(short op1, byte op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(short op1, short op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(short op1, int op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static long multiply(short op1, long op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : (long)op1 * op2;
   }

   private static long multiplySlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static double multiply(short op1, float op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(short op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(short op1, double op2) {
      return instance.short_multiply ? multiplySlow(op1, op2) : (double)op1 * op2;
   }

   private static double multiplySlow(short op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static int multiply(int op1, byte op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(int op1, short op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static int multiply(int op1, int op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static int multiplySlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).intValue();
   }

   public static long multiply(int op1, long op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : (long)op1 * op2;
   }

   private static long multiplySlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static double multiply(int op1, float op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(int op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(int op1, double op2) {
      return instance.int_multiply ? multiplySlow(op1, op2) : (double)op1 * op2;
   }

   private static double multiplySlow(int op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static long multiply(long op1, byte op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : op1 * (long)op2;
   }

   private static long multiplySlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static long multiply(long op1, short op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : op1 * (long)op2;
   }

   private static long multiplySlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static long multiply(long op1, int op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : op1 * (long)op2;
   }

   private static long multiplySlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static long multiply(long op1, long op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static long multiplySlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).longValue();
   }

   public static double multiply(long op1, float op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(long op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(long op1, double op2) {
      return instance.long_multiply ? multiplySlow(op1, op2) : (double)op1 * op2;
   }

   private static double multiplySlow(long op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, byte op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(float op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, short op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(float op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, int op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(float op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, long op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(float op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, float op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * (double)op2;
   }

   private static double multiplySlow(float op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(float op1, double op2) {
      return instance.float_multiply ? multiplySlow(op1, op2) : (double)op1 * op2;
   }

   private static double multiplySlow(float op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, byte op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * (double)op2;
   }

   private static double multiplySlow(double op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, short op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * (double)op2;
   }

   private static double multiplySlow(double op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, int op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * (double)op2;
   }

   private static double multiplySlow(double op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, long op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * (double)op2;
   }

   private static double multiplySlow(double op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, float op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * (double)op2;
   }

   private static double multiplySlow(double op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static double multiply(double op1, double op2) {
      return instance.double_multiply ? multiplySlow(op1, op2) : op1 * op2;
   }

   private static double multiplySlow(double op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "multiply", op2)).doubleValue();
   }

   public static int div(byte op1, byte op2) {
      return instance.byte_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(byte op1, short op2) {
      return instance.byte_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(byte op1, int op2) {
      return instance.byte_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static long div(byte op1, long op2) {
      return instance.byte_div ? divSlow(op1, op2) : (long)op1 / op2;
   }

   private static long divSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static double div(byte op1, float op2) {
      return instance.byte_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(byte op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(byte op1, double op2) {
      return instance.byte_div ? divSlow(op1, op2) : (double)op1 / op2;
   }

   private static double divSlow(byte op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static int div(short op1, byte op2) {
      return instance.short_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(short op1, short op2) {
      return instance.short_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(short op1, int op2) {
      return instance.short_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static long div(short op1, long op2) {
      return instance.short_div ? divSlow(op1, op2) : (long)op1 / op2;
   }

   private static long divSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static double div(short op1, float op2) {
      return instance.short_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(short op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(short op1, double op2) {
      return instance.short_div ? divSlow(op1, op2) : (double)op1 / op2;
   }

   private static double divSlow(short op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static int div(int op1, byte op2) {
      return instance.int_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(int op1, short op2) {
      return instance.int_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static int div(int op1, int op2) {
      return instance.int_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static int divSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).intValue();
   }

   public static long div(int op1, long op2) {
      return instance.int_div ? divSlow(op1, op2) : (long)op1 / op2;
   }

   private static long divSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static double div(int op1, float op2) {
      return instance.int_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(int op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(int op1, double op2) {
      return instance.int_div ? divSlow(op1, op2) : (double)op1 / op2;
   }

   private static double divSlow(int op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static long div(long op1, byte op2) {
      return instance.long_div ? divSlow(op1, op2) : op1 / (long)op2;
   }

   private static long divSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static long div(long op1, short op2) {
      return instance.long_div ? divSlow(op1, op2) : op1 / (long)op2;
   }

   private static long divSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static long div(long op1, int op2) {
      return instance.long_div ? divSlow(op1, op2) : op1 / (long)op2;
   }

   private static long divSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static long div(long op1, long op2) {
      return instance.long_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static long divSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).longValue();
   }

   public static double div(long op1, float op2) {
      return instance.long_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(long op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(long op1, double op2) {
      return instance.long_div ? divSlow(op1, op2) : (double)op1 / op2;
   }

   private static double divSlow(long op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, byte op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(float op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, short op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(float op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, int op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(float op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, long op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(float op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, float op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / (double)op2;
   }

   private static double divSlow(float op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(float op1, double op2) {
      return instance.float_div ? divSlow(op1, op2) : (double)op1 / op2;
   }

   private static double divSlow(float op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, byte op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / (double)op2;
   }

   private static double divSlow(double op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, short op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / (double)op2;
   }

   private static double divSlow(double op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, int op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / (double)op2;
   }

   private static double divSlow(double op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, long op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / (double)op2;
   }

   private static double divSlow(double op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, float op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / (double)op2;
   }

   private static double divSlow(double op1, float op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static double div(double op1, double op2) {
      return instance.double_div ? divSlow(op1, op2) : op1 / op2;
   }

   private static double divSlow(double op1, double op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "div", op2)).doubleValue();
   }

   public static int or(byte op1, byte op2) {
      return instance.byte_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(byte op1, short op2) {
      return instance.byte_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(byte op1, int op2) {
      return instance.byte_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static long or(byte op1, long op2) {
      return instance.byte_or ? orSlow(op1, op2) : (long)op1 | op2;
   }

   private static long orSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static int or(short op1, byte op2) {
      return instance.short_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(short op1, short op2) {
      return instance.short_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(short op1, int op2) {
      return instance.short_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static long or(short op1, long op2) {
      return instance.short_or ? orSlow(op1, op2) : (long)op1 | op2;
   }

   private static long orSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static int or(int op1, byte op2) {
      return instance.int_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(int op1, short op2) {
      return instance.int_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static int or(int op1, int op2) {
      return instance.int_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static int orSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).intValue();
   }

   public static long or(int op1, long op2) {
      return instance.int_or ? orSlow(op1, op2) : (long)op1 | op2;
   }

   private static long orSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static long or(long op1, byte op2) {
      return instance.long_or ? orSlow(op1, op2) : op1 | (long)op2;
   }

   private static long orSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static long or(long op1, short op2) {
      return instance.long_or ? orSlow(op1, op2) : op1 | (long)op2;
   }

   private static long orSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static long or(long op1, int op2) {
      return instance.long_or ? orSlow(op1, op2) : op1 | (long)op2;
   }

   private static long orSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static long or(long op1, long op2) {
      return instance.long_or ? orSlow(op1, op2) : op1 | op2;
   }

   private static long orSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "or", op2)).longValue();
   }

   public static int and(byte op1, byte op2) {
      return instance.byte_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(byte op1, short op2) {
      return instance.byte_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(byte op1, int op2) {
      return instance.byte_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static long and(byte op1, long op2) {
      return instance.byte_and ? andSlow(op1, op2) : (long)op1 & op2;
   }

   private static long andSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static int and(short op1, byte op2) {
      return instance.short_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(short op1, short op2) {
      return instance.short_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(short op1, int op2) {
      return instance.short_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static long and(short op1, long op2) {
      return instance.short_and ? andSlow(op1, op2) : (long)op1 & op2;
   }

   private static long andSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static int and(int op1, byte op2) {
      return instance.int_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(int op1, short op2) {
      return instance.int_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static int and(int op1, int op2) {
      return instance.int_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static int andSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).intValue();
   }

   public static long and(int op1, long op2) {
      return instance.int_and ? andSlow(op1, op2) : (long)op1 & op2;
   }

   private static long andSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static long and(long op1, byte op2) {
      return instance.long_and ? andSlow(op1, op2) : op1 & (long)op2;
   }

   private static long andSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static long and(long op1, short op2) {
      return instance.long_and ? andSlow(op1, op2) : op1 & (long)op2;
   }

   private static long andSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static long and(long op1, int op2) {
      return instance.long_and ? andSlow(op1, op2) : op1 & (long)op2;
   }

   private static long andSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static long and(long op1, long op2) {
      return instance.long_and ? andSlow(op1, op2) : op1 & op2;
   }

   private static long andSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "and", op2)).longValue();
   }

   public static int xor(byte op1, byte op2) {
      return instance.byte_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(byte op1, short op2) {
      return instance.byte_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(byte op1, int op2) {
      return instance.byte_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static long xor(byte op1, long op2) {
      return instance.byte_xor ? xorSlow(op1, op2) : (long)op1 ^ op2;
   }

   private static long xorSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static int xor(short op1, byte op2) {
      return instance.short_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(short op1, short op2) {
      return instance.short_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(short op1, int op2) {
      return instance.short_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static long xor(short op1, long op2) {
      return instance.short_xor ? xorSlow(op1, op2) : (long)op1 ^ op2;
   }

   private static long xorSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static int xor(int op1, byte op2) {
      return instance.int_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(int op1, short op2) {
      return instance.int_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static int xor(int op1, int op2) {
      return instance.int_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static int xorSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).intValue();
   }

   public static long xor(int op1, long op2) {
      return instance.int_xor ? xorSlow(op1, op2) : (long)op1 ^ op2;
   }

   private static long xorSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static long xor(long op1, byte op2) {
      return instance.long_xor ? xorSlow(op1, op2) : op1 ^ (long)op2;
   }

   private static long xorSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static long xor(long op1, short op2) {
      return instance.long_xor ? xorSlow(op1, op2) : op1 ^ (long)op2;
   }

   private static long xorSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static long xor(long op1, int op2) {
      return instance.long_xor ? xorSlow(op1, op2) : op1 ^ (long)op2;
   }

   private static long xorSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static long xor(long op1, long op2) {
      return instance.long_xor ? xorSlow(op1, op2) : op1 ^ op2;
   }

   private static long xorSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "xor", op2)).longValue();
   }

   public static int intdiv(byte op1, byte op2) {
      return instance.byte_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(byte op1, short op2) {
      return instance.byte_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(byte op1, int op2) {
      return instance.byte_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static long intdiv(byte op1, long op2) {
      return instance.byte_intdiv ? intdivSlow(op1, op2) : (long)op1 / op2;
   }

   private static long intdivSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static int intdiv(short op1, byte op2) {
      return instance.short_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(short op1, short op2) {
      return instance.short_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(short op1, int op2) {
      return instance.short_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static long intdiv(short op1, long op2) {
      return instance.short_intdiv ? intdivSlow(op1, op2) : (long)op1 / op2;
   }

   private static long intdivSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static int intdiv(int op1, byte op2) {
      return instance.int_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(int op1, short op2) {
      return instance.int_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static int intdiv(int op1, int op2) {
      return instance.int_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static int intdivSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).intValue();
   }

   public static long intdiv(int op1, long op2) {
      return instance.int_intdiv ? intdivSlow(op1, op2) : (long)op1 / op2;
   }

   private static long intdivSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static long intdiv(long op1, byte op2) {
      return instance.long_intdiv ? intdivSlow(op1, op2) : op1 / (long)op2;
   }

   private static long intdivSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static long intdiv(long op1, short op2) {
      return instance.long_intdiv ? intdivSlow(op1, op2) : op1 / (long)op2;
   }

   private static long intdivSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static long intdiv(long op1, int op2) {
      return instance.long_intdiv ? intdivSlow(op1, op2) : op1 / (long)op2;
   }

   private static long intdivSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static long intdiv(long op1, long op2) {
      return instance.long_intdiv ? intdivSlow(op1, op2) : op1 / op2;
   }

   private static long intdivSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "intdiv", op2)).longValue();
   }

   public static int mod(byte op1, byte op2) {
      return instance.byte_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(byte op1, short op2) {
      return instance.byte_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(byte op1, int op2) {
      return instance.byte_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static long mod(byte op1, long op2) {
      return instance.byte_mod ? modSlow(op1, op2) : (long)op1 % op2;
   }

   private static long modSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static int mod(short op1, byte op2) {
      return instance.short_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(short op1, short op2) {
      return instance.short_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(short op1, int op2) {
      return instance.short_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static long mod(short op1, long op2) {
      return instance.short_mod ? modSlow(op1, op2) : (long)op1 % op2;
   }

   private static long modSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static int mod(int op1, byte op2) {
      return instance.int_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(int op1, short op2) {
      return instance.int_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static int mod(int op1, int op2) {
      return instance.int_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static int modSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).intValue();
   }

   public static long mod(int op1, long op2) {
      return instance.int_mod ? modSlow(op1, op2) : (long)op1 % op2;
   }

   private static long modSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static long mod(long op1, byte op2) {
      return instance.long_mod ? modSlow(op1, op2) : op1 % (long)op2;
   }

   private static long modSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static long mod(long op1, short op2) {
      return instance.long_mod ? modSlow(op1, op2) : op1 % (long)op2;
   }

   private static long modSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static long mod(long op1, int op2) {
      return instance.long_mod ? modSlow(op1, op2) : op1 % (long)op2;
   }

   private static long modSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static long mod(long op1, long op2) {
      return instance.long_mod ? modSlow(op1, op2) : op1 % op2;
   }

   private static long modSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "mod", op2)).longValue();
   }

   public static int leftShift(byte op1, byte op2) {
      return instance.byte_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(byte op1, short op2) {
      return instance.byte_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(byte op1, int op2) {
      return instance.byte_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static long leftShift(byte op1, long op2) {
      return instance.byte_leftShift ? leftShiftSlow(op1, op2) : (long)op1 << (int)op2;
   }

   private static long leftShiftSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static int leftShift(short op1, byte op2) {
      return instance.short_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(short op1, short op2) {
      return instance.short_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(short op1, int op2) {
      return instance.short_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static long leftShift(short op1, long op2) {
      return instance.short_leftShift ? leftShiftSlow(op1, op2) : (long)op1 << (int)op2;
   }

   private static long leftShiftSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static int leftShift(int op1, byte op2) {
      return instance.int_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(int op1, short op2) {
      return instance.int_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static int leftShift(int op1, int op2) {
      return instance.int_leftShift ? leftShiftSlow(op1, op2) : op1 << op2;
   }

   private static int leftShiftSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).intValue();
   }

   public static long leftShift(int op1, long op2) {
      return instance.int_leftShift ? leftShiftSlow(op1, op2) : (long)op1 << (int)op2;
   }

   private static long leftShiftSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static long leftShift(long op1, byte op2) {
      return instance.long_leftShift ? leftShiftSlow(op1, op2) : op1 << (int)((long)op2);
   }

   private static long leftShiftSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static long leftShift(long op1, short op2) {
      return instance.long_leftShift ? leftShiftSlow(op1, op2) : op1 << (int)((long)op2);
   }

   private static long leftShiftSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static long leftShift(long op1, int op2) {
      return instance.long_leftShift ? leftShiftSlow(op1, op2) : op1 << (int)((long)op2);
   }

   private static long leftShiftSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static long leftShift(long op1, long op2) {
      return instance.long_leftShift ? leftShiftSlow(op1, op2) : op1 << (int)op2;
   }

   private static long leftShiftSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "leftShift", op2)).longValue();
   }

   public static int rightShift(byte op1, byte op2) {
      return instance.byte_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(byte op1, short op2) {
      return instance.byte_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(byte op1, int op2) {
      return instance.byte_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static long rightShift(byte op1, long op2) {
      return instance.byte_rightShift ? rightShiftSlow(op1, op2) : (long)op1 >> (int)op2;
   }

   private static long rightShiftSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static int rightShift(short op1, byte op2) {
      return instance.short_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(short op1, short op2) {
      return instance.short_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(short op1, int op2) {
      return instance.short_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static long rightShift(short op1, long op2) {
      return instance.short_rightShift ? rightShiftSlow(op1, op2) : (long)op1 >> (int)op2;
   }

   private static long rightShiftSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static int rightShift(int op1, byte op2) {
      return instance.int_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(int op1, short op2) {
      return instance.int_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static int rightShift(int op1, int op2) {
      return instance.int_rightShift ? rightShiftSlow(op1, op2) : op1 >> op2;
   }

   private static int rightShiftSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).intValue();
   }

   public static long rightShift(int op1, long op2) {
      return instance.int_rightShift ? rightShiftSlow(op1, op2) : (long)op1 >> (int)op2;
   }

   private static long rightShiftSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static long rightShift(long op1, byte op2) {
      return instance.long_rightShift ? rightShiftSlow(op1, op2) : op1 >> (int)((long)op2);
   }

   private static long rightShiftSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static long rightShift(long op1, short op2) {
      return instance.long_rightShift ? rightShiftSlow(op1, op2) : op1 >> (int)((long)op2);
   }

   private static long rightShiftSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static long rightShift(long op1, int op2) {
      return instance.long_rightShift ? rightShiftSlow(op1, op2) : op1 >> (int)((long)op2);
   }

   private static long rightShiftSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static long rightShift(long op1, long op2) {
      return instance.long_rightShift ? rightShiftSlow(op1, op2) : op1 >> (int)op2;
   }

   private static long rightShiftSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShift", op2)).longValue();
   }

   public static int rightShiftUnsigned(byte op1, byte op2) {
      return instance.byte_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(byte op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(byte op1, short op2) {
      return instance.byte_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(byte op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(byte op1, int op2) {
      return instance.byte_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(byte op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static long rightShiftUnsigned(byte op1, long op2) {
      return instance.byte_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : (long)op1 >>> (int)op2;
   }

   private static long rightShiftUnsignedSlow(byte op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static int rightShiftUnsigned(short op1, byte op2) {
      return instance.short_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(short op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(short op1, short op2) {
      return instance.short_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(short op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(short op1, int op2) {
      return instance.short_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(short op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static long rightShiftUnsigned(short op1, long op2) {
      return instance.short_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : (long)op1 >>> (int)op2;
   }

   private static long rightShiftUnsignedSlow(short op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static int rightShiftUnsigned(int op1, byte op2) {
      return instance.int_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(int op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(int op1, short op2) {
      return instance.int_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(int op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static int rightShiftUnsigned(int op1, int op2) {
      return instance.int_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> op2;
   }

   private static int rightShiftUnsignedSlow(int op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).intValue();
   }

   public static long rightShiftUnsigned(int op1, long op2) {
      return instance.int_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : (long)op1 >>> (int)op2;
   }

   private static long rightShiftUnsignedSlow(int op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static long rightShiftUnsigned(long op1, byte op2) {
      return instance.long_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> (int)((long)op2);
   }

   private static long rightShiftUnsignedSlow(long op1, byte op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static long rightShiftUnsigned(long op1, short op2) {
      return instance.long_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> (int)((long)op2);
   }

   private static long rightShiftUnsignedSlow(long op1, short op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static long rightShiftUnsigned(long op1, int op2) {
      return instance.long_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> (int)((long)op2);
   }

   private static long rightShiftUnsignedSlow(long op1, int op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }

   public static long rightShiftUnsigned(long op1, long op2) {
      return instance.long_rightShiftUnsigned ? rightShiftUnsignedSlow(op1, op2) : op1 >>> (int)op2;
   }

   private static long rightShiftUnsignedSlow(long op1, long op2) {
      return ((Number)InvokerHelper.invokeMethod(op1, "rightShiftUnsigned", op2)).longValue();
   }
}
