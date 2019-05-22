package org.apache.commons.validator;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericTypeValidator implements Serializable {
   private static final Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$GenericTypeValidator;

   public static Byte formatByte(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Byte(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Byte formatByte(String value, Locale locale) {
      Byte result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getNumberInstance(locale);
         } else {
            formatter = NumberFormat.getNumberInstance(Locale.getDefault());
         }

         formatter.setParseIntegerOnly(true);
         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -128.0D && num.doubleValue() <= 127.0D) {
            result = new Byte(num.byteValue());
         }
      }

      return result;
   }

   public static Short formatShort(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Short(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Short formatShort(String value, Locale locale) {
      Short result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getNumberInstance(locale);
         } else {
            formatter = NumberFormat.getNumberInstance(Locale.getDefault());
         }

         formatter.setParseIntegerOnly(true);
         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -32768.0D && num.doubleValue() <= 32767.0D) {
            result = new Short(num.shortValue());
         }
      }

      return result;
   }

   public static Integer formatInt(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Integer(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Integer formatInt(String value, Locale locale) {
      Integer result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getNumberInstance(locale);
         } else {
            formatter = NumberFormat.getNumberInstance(Locale.getDefault());
         }

         formatter.setParseIntegerOnly(true);
         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -2.147483648E9D && num.doubleValue() <= 2.147483647E9D) {
            result = new Integer(num.intValue());
         }
      }

      return result;
   }

   public static Long formatLong(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Long(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Long formatLong(String value, Locale locale) {
      Long result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getNumberInstance(locale);
         } else {
            formatter = NumberFormat.getNumberInstance(Locale.getDefault());
         }

         formatter.setParseIntegerOnly(true);
         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -9.223372036854776E18D && num.doubleValue() <= 9.223372036854776E18D) {
            result = new Long(num.longValue());
         }
      }

      return result;
   }

   public static Float formatFloat(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Float(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Float formatFloat(String value, Locale locale) {
      Float result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getInstance(locale);
         } else {
            formatter = NumberFormat.getInstance(Locale.getDefault());
         }

         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -3.4028234663852886E38D && num.doubleValue() <= 3.4028234663852886E38D) {
            result = new Float(num.floatValue());
         }
      }

      return result;
   }

   public static Double formatDouble(String value) {
      if (value == null) {
         return null;
      } else {
         try {
            return new Double(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Double formatDouble(String value, Locale locale) {
      Double result = null;
      if (value != null) {
         NumberFormat formatter = null;
         if (locale != null) {
            formatter = NumberFormat.getInstance(locale);
         } else {
            formatter = NumberFormat.getInstance(Locale.getDefault());
         }

         ParsePosition pos = new ParsePosition(0);
         Number num = formatter.parse(value, pos);
         if (pos.getErrorIndex() == -1 && pos.getIndex() == value.length() && num.doubleValue() >= -1.7976931348623157E308D && num.doubleValue() <= Double.MAX_VALUE) {
            result = new Double(num.doubleValue());
         }
      }

      return result;
   }

   public static Date formatDate(String value, Locale locale) {
      Date date = null;
      if (value == null) {
         return null;
      } else {
         try {
            DateFormat formatter = null;
            if (locale != null) {
               formatter = DateFormat.getDateInstance(3, locale);
            } else {
               formatter = DateFormat.getDateInstance(3, Locale.getDefault());
            }

            formatter.setLenient(false);
            date = formatter.parse(value);
         } catch (ParseException var4) {
            if (log.isDebugEnabled()) {
               log.debug("Date parse failed value=[" + value + "], " + "locale=[" + locale + "] " + var4);
            }
         }

         return date;
      }
   }

   public static Date formatDate(String value, String datePattern, boolean strict) {
      Date date = null;
      if (value != null && datePattern != null && datePattern.length() != 0) {
         try {
            SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
            formatter.setLenient(false);
            date = formatter.parse(value);
            if (strict && datePattern.length() != value.length()) {
               date = null;
            }
         } catch (ParseException var5) {
            if (log.isDebugEnabled()) {
               log.debug("Date parse failed value=[" + value + "], " + "pattern=[" + datePattern + "], " + "strict=[" + strict + "] " + var5);
            }
         }

         return date;
      } else {
         return null;
      }
   }

   public static Long formatCreditCard(String value) {
      return GenericValidator.isCreditCard(value) ? new Long(value) : null;
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
      log = LogFactory.getLog(class$org$apache$commons$validator$GenericTypeValidator == null ? (class$org$apache$commons$validator$GenericTypeValidator = class$("org.apache.commons.validator.GenericTypeValidator")) : class$org$apache$commons$validator$GenericTypeValidator);
   }
}
