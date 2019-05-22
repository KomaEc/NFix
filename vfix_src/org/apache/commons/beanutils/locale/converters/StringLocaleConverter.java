package org.apache.commons.beanutils.locale.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.beanutils.locale.BaseLocaleConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringLocaleConverter extends BaseLocaleConverter {
   private static Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$converters$StringLocaleConverter;

   public StringLocaleConverter() {
      this(false);
   }

   public StringLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public StringLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public StringLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public StringLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public StringLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public StringLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public StringLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public StringLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public StringLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public StringLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public StringLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern, locPattern);
   }

   protected Object parse(Object value, String pattern) throws ParseException {
      String result = null;
      if (!(value instanceof Integer) && !(value instanceof Long) && !(value instanceof BigInteger) && !(value instanceof Byte) && !(value instanceof Short)) {
         if (!(value instanceof Double) && !(value instanceof BigDecimal) && !(value instanceof Float)) {
            if (value instanceof Date) {
               SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, this.locale);
               result = dateFormat.format(value);
            } else {
               result = value.toString();
            }
         } else {
            result = this.getDecimalFormat(this.locale, pattern).format(((Number)value).doubleValue());
         }
      } else {
         result = this.getDecimalFormat(this.locale, pattern).format(((Number)value).longValue());
      }

      return result;
   }

   private DecimalFormat getDecimalFormat(Locale locale, String pattern) {
      DecimalFormat numberFormat = (DecimalFormat)NumberFormat.getInstance(locale);
      if (pattern != null) {
         if (this.locPattern) {
            numberFormat.applyLocalizedPattern(pattern);
         } else {
            numberFormat.applyPattern(pattern);
         }
      } else {
         log.warn("No pattern provided, using default.");
      }

      return numberFormat;
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
      log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$converters$StringLocaleConverter == null ? (class$org$apache$commons$beanutils$locale$converters$StringLocaleConverter = class$("org.apache.commons.beanutils.locale.converters.StringLocaleConverter")) : class$org$apache$commons$beanutils$locale$converters$StringLocaleConverter);
   }
}
