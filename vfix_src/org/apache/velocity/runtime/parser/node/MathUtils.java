package org.apache.velocity.runtime.parser.node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MathUtils {
   protected static final BigDecimal DECIMAL_ZERO;
   protected static final int BASE_LONG = 0;
   protected static final int BASE_FLOAT = 1;
   protected static final int BASE_DOUBLE = 2;
   protected static final int BASE_BIGINTEGER = 3;
   protected static final int BASE_BIGDECIMAL = 4;
   protected static final Map ints;
   protected static final List typesBySize;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$math$BigInteger;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Double;

   public static BigDecimal toBigDecimal(Number n) {
      if (n instanceof BigDecimal) {
         return (BigDecimal)n;
      } else {
         return n instanceof BigInteger ? new BigDecimal((BigInteger)n) : new BigDecimal(n.doubleValue());
      }
   }

   public static BigInteger toBigInteger(Number n) {
      return n instanceof BigInteger ? (BigInteger)n : BigInteger.valueOf(n.longValue());
   }

   public static boolean isZero(Number n) {
      if (isInteger(n)) {
         if (n instanceof BigInteger) {
            return ((BigInteger)n).compareTo(BigInteger.ZERO) == 0;
         } else {
            return n.doubleValue() == 0.0D;
         }
      } else if (n instanceof Float) {
         return n.floatValue() == 0.0F;
      } else if (n instanceof Double) {
         return n.doubleValue() == 0.0D;
      } else {
         return toBigDecimal(n).compareTo(DECIMAL_ZERO) == 0;
      }
   }

   public static boolean isInteger(Number n) {
      return ints.containsKey(n.getClass());
   }

   public static Number wrapPrimitive(long value, Class type) {
      if (type == (class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte)) {
         if (value <= 127L && value >= -128L) {
            return new Byte((byte)((int)value));
         }

         type = class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short;
      }

      if (type == (class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short)) {
         if (value <= 32767L && value >= -32768L) {
            return new Short((short)((int)value));
         }

         type = class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer;
      }

      if (type == (class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer)) {
         if (value <= 2147483647L && value >= -2147483648L) {
            return new Integer((int)value);
         }

         type = class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long;
      }

      return (Number)(type == (class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long) ? new Long(value) : BigInteger.valueOf(value));
   }

   private static Number wrapPrimitive(long value, Number op1, Number op2) {
      return typesBySize.indexOf(op1.getClass()) > typesBySize.indexOf(op2.getClass()) ? wrapPrimitive(value, op1.getClass()) : wrapPrimitive(value, op2.getClass());
   }

   private static int findCalculationBase(Number op1, Number op2) {
      boolean op1Int = isInteger(op1);
      boolean op2Int = isInteger(op2);
      if (!(op1 instanceof BigDecimal) && !(op2 instanceof BigDecimal) && (op1Int && op2Int || !(op1 instanceof BigInteger) && !(op2 instanceof BigInteger))) {
         if (op1Int && op2Int) {
            return !(op1 instanceof BigInteger) && !(op2 instanceof BigInteger) ? 0 : 3;
         } else {
            return !(op1 instanceof Double) && !(op2 instanceof Double) ? 1 : 2;
         }
      } else {
         return 4;
      }
   }

   public static Number add(Number op1, Number op2) {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         long l1 = op1.longValue();
         long l2 = op2.longValue();
         long result = l1 + l2;
         if ((result ^ l1) < 0L && (result ^ l2) < 0L) {
            return toBigInteger(op1).add(toBigInteger(op2));
         }

         return wrapPrimitive(result, op1, op2);
      case 1:
         return new Float(op1.floatValue() + op2.floatValue());
      case 2:
         return new Double(op1.doubleValue() + op2.doubleValue());
      case 3:
         return toBigInteger(op1).add(toBigInteger(op2));
      default:
         return toBigDecimal(op1).add(toBigDecimal(op2));
      }
   }

   public static Number subtract(Number op1, Number op2) {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         long l1 = op1.longValue();
         long l2 = op2.longValue();
         long result = l1 - l2;
         if ((result ^ l1) < 0L && (result ^ ~l2) < 0L) {
            return toBigInteger(op1).subtract(toBigInteger(op2));
         }

         return wrapPrimitive(result, op1, op2);
      case 1:
         return new Float(op1.floatValue() - op2.floatValue());
      case 2:
         return new Double(op1.doubleValue() - op2.doubleValue());
      case 3:
         return toBigInteger(op1).subtract(toBigInteger(op2));
      default:
         return toBigDecimal(op1).subtract(toBigDecimal(op2));
      }
   }

   public static Number multiply(Number op1, Number op2) {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         long l1 = op1.longValue();
         long l2 = op2.longValue();
         long result = l1 * l2;
         if (l2 != 0L && result / l2 != l1) {
            return toBigInteger(op1).multiply(toBigInteger(op2));
         }

         return wrapPrimitive(result, op1, op2);
      case 1:
         return new Float(op1.floatValue() * op2.floatValue());
      case 2:
         return new Double(op1.doubleValue() * op2.doubleValue());
      case 3:
         return toBigInteger(op1).multiply(toBigInteger(op2));
      default:
         return toBigDecimal(op1).multiply(toBigDecimal(op2));
      }
   }

   public static Number divide(Number op1, Number op2) {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         long l1 = op1.longValue();
         long l2 = op2.longValue();
         return wrapPrimitive(l1 / l2, op1, op2);
      case 1:
         return new Float(op1.floatValue() / op2.floatValue());
      case 2:
         return new Double(op1.doubleValue() / op2.doubleValue());
      case 3:
         BigInteger b1 = toBigInteger(op1);
         BigInteger b2 = toBigInteger(op2);
         return b1.divide(b2);
      default:
         return toBigDecimal(op1).divide(toBigDecimal(op2), 5);
      }
   }

   public static Number modulo(Number op1, Number op2) throws ArithmeticException {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         return wrapPrimitive(op1.longValue() % op2.longValue(), op1, op2);
      case 1:
         return new Float(op1.floatValue() % op2.floatValue());
      case 2:
         return new Double(op1.doubleValue() % op2.doubleValue());
      case 3:
         return toBigInteger(op1).mod(toBigInteger(op2));
      default:
         throw new ArithmeticException("Cannot calculate the modulo of BigDecimals.");
      }
   }

   public static int compare(Number op1, Number op2) {
      int calcBase = findCalculationBase(op1, op2);
      switch(calcBase) {
      case 0:
         long l1 = op1.longValue();
         long l2 = op2.longValue();
         if (l1 < l2) {
            return -1;
         } else {
            if (l1 > l2) {
               return 1;
            }

            return 0;
         }
      case 1:
         float f1 = op1.floatValue();
         float f2 = op2.floatValue();
         if (f1 < f2) {
            return -1;
         } else {
            if (f1 > f2) {
               return 1;
            }

            return 0;
         }
      case 2:
         double d1 = op1.doubleValue();
         double d2 = op2.doubleValue();
         if (d1 < d2) {
            return -1;
         } else {
            if (d1 > d2) {
               return 1;
            }

            return 0;
         }
      case 3:
         return toBigInteger(op1).compareTo(toBigInteger(op2));
      default:
         return toBigDecimal(op1).compareTo(toBigDecimal(op2));
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
      DECIMAL_ZERO = new BigDecimal(BigInteger.ZERO);
      ints = new HashMap();
      ints.put(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte, BigDecimal.valueOf(127L));
      ints.put(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short, BigDecimal.valueOf(32767L));
      ints.put(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer, BigDecimal.valueOf(2147483647L));
      ints.put(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long, BigDecimal.valueOf(Long.MAX_VALUE));
      ints.put(class$java$math$BigInteger == null ? (class$java$math$BigInteger = class$("java.math.BigInteger")) : class$java$math$BigInteger, BigDecimal.valueOf(-1L));
      typesBySize = new ArrayList();
      typesBySize.add(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte);
      typesBySize.add(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short);
      typesBySize.add(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer);
      typesBySize.add(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long);
      typesBySize.add(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float);
      typesBySize.add(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double);
   }
}
