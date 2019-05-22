package org.jf.util;

import java.text.DecimalFormat;

public class NumberUtils {
   private static final int canonicalFloatNaN = Float.floatToRawIntBits(Float.NaN);
   private static final int maxFloat = Float.floatToRawIntBits(Float.MAX_VALUE);
   private static final int piFloat = Float.floatToRawIntBits(3.1415927F);
   private static final int eFloat = Float.floatToRawIntBits(2.7182817F);
   private static final long canonicalDoubleNaN = Double.doubleToRawLongBits(Double.NaN);
   private static final long maxDouble = Double.doubleToLongBits(Double.MAX_VALUE);
   private static final long piDouble = Double.doubleToLongBits(3.141592653589793D);
   private static final long eDouble = Double.doubleToLongBits(2.718281828459045D);
   private static final DecimalFormat format = new DecimalFormat("0.####################E0");

   public static boolean isLikelyFloat(int value) {
      if (value != canonicalFloatNaN && value != maxFloat && value != piFloat && value != eFloat) {
         if (value != Integer.MAX_VALUE && value != Integer.MIN_VALUE) {
            int packageId = value >> 24;
            int resourceType = value >> 16 & 255;
            int resourceId = value & '\uffff';
            if ((packageId == 127 || packageId == 1) && resourceType < 31 && resourceId < 4095) {
               return false;
            } else {
               float floatValue = Float.intBitsToFloat(value);
               if (Float.isNaN(floatValue)) {
                  return false;
               } else {
                  String asInt = format.format((long)value);
                  String asFloat = format.format((double)floatValue);
                  int decimalPoint = asFloat.indexOf(46);
                  int exponent = asFloat.indexOf("E");
                  int zeros = asFloat.indexOf("000");
                  if (zeros > decimalPoint && zeros < exponent) {
                     asFloat = asFloat.substring(0, zeros) + asFloat.substring(exponent);
                  } else {
                     int nines = asFloat.indexOf("999");
                     if (nines > decimalPoint && nines < exponent) {
                        asFloat = asFloat.substring(0, nines) + asFloat.substring(exponent);
                     }
                  }

                  return asFloat.length() < asInt.length();
               }
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean isLikelyDouble(long value) {
      if (value != canonicalDoubleNaN && value != maxDouble && value != piDouble && value != eDouble) {
         if (value != Long.MAX_VALUE && value != Long.MIN_VALUE) {
            double doubleValue = Double.longBitsToDouble(value);
            if (Double.isNaN(doubleValue)) {
               return false;
            } else {
               String asLong = format.format(value);
               String asDouble = format.format(doubleValue);
               int decimalPoint = asDouble.indexOf(46);
               int exponent = asDouble.indexOf("E");
               int zeros = asDouble.indexOf("000");
               if (zeros > decimalPoint && zeros < exponent) {
                  asDouble = asDouble.substring(0, zeros) + asDouble.substring(exponent);
               } else {
                  int nines = asDouble.indexOf("999");
                  if (nines > decimalPoint && nines < exponent) {
                     asDouble = asDouble.substring(0, nines) + asDouble.substring(exponent);
                  }
               }

               return asDouble.length() < asLong.length();
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }
}
