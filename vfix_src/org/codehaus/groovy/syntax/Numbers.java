package org.codehaus.groovy.syntax;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Numbers {
   private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
   private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
   private static final BigInteger MAX_INTEGER = BigInteger.valueOf(2147483647L);
   private static final BigInteger MIN_INTEGER = BigInteger.valueOf(-2147483648L);
   private static final BigDecimal MAX_DOUBLE = new BigDecimal(String.valueOf(Double.MAX_VALUE));
   private static final BigDecimal MIN_DOUBLE;
   private static final BigDecimal MAX_FLOAT;
   private static final BigDecimal MIN_FLOAT;

   public static boolean isDigit(char c) {
      return c >= '0' && c <= '9';
   }

   public static boolean isOctalDigit(char c) {
      return c >= '0' && c <= '7';
   }

   public static boolean isHexDigit(char c) {
      return isDigit(c) || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f';
   }

   public static boolean isNumericTypeSpecifier(char c, boolean isDecimal) {
      if (isDecimal) {
         switch(c) {
         case 'D':
         case 'F':
         case 'G':
         case 'd':
         case 'f':
         case 'g':
            return true;
         }
      } else {
         switch(c) {
         case 'G':
         case 'I':
         case 'L':
         case 'g':
         case 'i':
         case 'l':
            return true;
         }
      }

      return false;
   }

   public static Number parseInteger(String text) {
      char c = true;
      int length = text.length();
      boolean negative = false;
      char c;
      if ((c = text.charAt(0)) == '-' || c == '+') {
         negative = c == '-';
         text = text.substring(1, length);
         --length;
      }

      int radix = 10;
      if (text.charAt(0) == '0' && length > 1) {
         if ((c = text.charAt(1)) != 'X' && c != 'x') {
            radix = 8;
         } else {
            radix = 16;
            text = text.substring(2, length);
            length -= 2;
         }
      }

      char type = 'x';
      if (isNumericTypeSpecifier(text.charAt(length - 1), false)) {
         type = Character.toLowerCase(text.charAt(length - 1));
         text = text.substring(0, length - 1);
         --length;
      }

      if (negative) {
         text = "-" + text;
      }

      switch(type) {
      case 'g':
         return new BigInteger(text, radix);
      case 'i':
         return Integer.parseInt(text, radix);
      case 'l':
         return new Long(Long.parseLong(text, radix));
      default:
         BigInteger value = new BigInteger(text, radix);
         if (value.compareTo(MAX_INTEGER) <= 0 && value.compareTo(MIN_INTEGER) >= 0) {
            return value.intValue();
         } else {
            return (Number)(value.compareTo(MAX_LONG) <= 0 && value.compareTo(MIN_LONG) >= 0 ? new Long(value.longValue()) : value);
         }
      }
   }

   public static Number parseDecimal(String text) {
      int length = text.length();
      char type = 'x';
      if (isNumericTypeSpecifier(text.charAt(length - 1), true)) {
         type = Character.toLowerCase(text.charAt(length - 1));
         text = text.substring(0, length - 1);
         --length;
      }

      BigDecimal value = new BigDecimal(text);
      switch(type) {
      case 'd':
         if (value.compareTo(MAX_DOUBLE) <= 0 && value.compareTo(MIN_DOUBLE) >= 0) {
            return new Double(text);
         }

         throw new NumberFormatException("out of range");
      case 'e':
      case 'g':
      default:
         return value;
      case 'f':
         if (value.compareTo(MAX_FLOAT) <= 0 && value.compareTo(MIN_FLOAT) >= 0) {
            return new Float(text);
         } else {
            throw new NumberFormatException("out of range");
         }
      }
   }

   static {
      MIN_DOUBLE = MAX_DOUBLE.negate();
      MAX_FLOAT = new BigDecimal(String.valueOf(Float.MAX_VALUE));
      MIN_FLOAT = MAX_FLOAT.negate();
   }
}
