package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Locale;
import org.apache.oro.text.perl.Perl5Util;

public class GenericValidator implements Serializable {
   private static final UrlValidator urlValidator = new UrlValidator();
   private static final CreditCardValidator creditCardValidator = new CreditCardValidator();

   public static boolean isBlankOrNull(String value) {
      return value == null || value.trim().length() == 0;
   }

   public static boolean matchRegexp(String value, String regexp) {
      if (regexp != null && regexp.length() > 0) {
         Perl5Util matcher = new Perl5Util();
         return matcher.match("/" + regexp + "/", value);
      } else {
         return false;
      }
   }

   public static boolean isByte(String value) {
      return GenericTypeValidator.formatByte(value) != null;
   }

   public static boolean isShort(String value) {
      return GenericTypeValidator.formatShort(value) != null;
   }

   public static boolean isInt(String value) {
      return GenericTypeValidator.formatInt(value) != null;
   }

   public static boolean isLong(String value) {
      return GenericTypeValidator.formatLong(value) != null;
   }

   public static boolean isFloat(String value) {
      return GenericTypeValidator.formatFloat(value) != null;
   }

   public static boolean isDouble(String value) {
      return GenericTypeValidator.formatDouble(value) != null;
   }

   public static boolean isDate(String value, Locale locale) {
      return DateValidator.getInstance().isValid(value, locale);
   }

   public static boolean isDate(String value, String datePattern, boolean strict) {
      return DateValidator.getInstance().isValid(value, datePattern, strict);
   }

   public static boolean isInRange(byte value, byte min, byte max) {
      return value >= min && value <= max;
   }

   public static boolean isInRange(int value, int min, int max) {
      return value >= min && value <= max;
   }

   public static boolean isInRange(float value, float min, float max) {
      return value >= min && value <= max;
   }

   public static boolean isInRange(short value, short min, short max) {
      return value >= min && value <= max;
   }

   public static boolean isInRange(long value, long min, long max) {
      return value >= min && value <= max;
   }

   public static boolean isInRange(double value, double min, double max) {
      return value >= min && value <= max;
   }

   public static boolean isCreditCard(String value) {
      return creditCardValidator.isValid(value);
   }

   public static boolean isEmail(String value) {
      return EmailValidator.getInstance().isValid(value);
   }

   public static boolean isUrl(String value) {
      return urlValidator.isValid(value);
   }

   public static boolean maxLength(String value, int max) {
      return value.length() <= max;
   }

   public static boolean minLength(String value, int min) {
      return value.length() >= min;
   }

   public static boolean minValue(int value, int min) {
      return value >= min;
   }

   public static boolean minValue(long value, long min) {
      return value >= min;
   }

   public static boolean minValue(double value, double min) {
      return value >= min;
   }

   public static boolean minValue(float value, float min) {
      return value >= min;
   }

   public static boolean maxValue(int value, int max) {
      return value <= max;
   }

   public static boolean maxValue(long value, long max) {
      return value <= max;
   }

   public static boolean maxValue(double value, double max) {
      return value <= max;
   }

   public static boolean maxValue(float value, float max) {
      return value <= max;
   }
}
