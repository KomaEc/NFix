package org.apache.commons.beanutils.locale.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.commons.beanutils.locale.BaseLocaleConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateLocaleConverter extends BaseLocaleConverter {
   private static Log log;
   boolean isLenient;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$converters$DateLocaleConverter;

   public DateLocaleConverter() {
      this(false);
   }

   public DateLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public DateLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public DateLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public DateLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public DateLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
      this.isLenient = false;
   }

   public DateLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public DateLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public DateLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public DateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public DateLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public DateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern, locPattern);
      this.isLenient = false;
   }

   public boolean isLenient() {
      return this.isLenient;
   }

   public void setLenient(boolean lenient) {
      this.isLenient = lenient;
   }

   protected Object parse(Object value, String pattern) throws ParseException {
      SimpleDateFormat formatter = this.getFormatter(pattern, this.locale);
      if (this.locPattern) {
         formatter.applyLocalizedPattern(pattern);
      } else {
         formatter.applyPattern(pattern);
      }

      return formatter.parse((String)value);
   }

   private SimpleDateFormat getFormatter(String pattern, Locale locale) {
      if (pattern == null) {
         pattern = this.locPattern ? (new SimpleDateFormat()).toLocalizedPattern() : (new SimpleDateFormat()).toPattern();
         log.warn("Null pattern was provided, defaulting to: " + pattern);
      }

      SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
      format.setLenient(this.isLenient);
      return format;
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
      log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$converters$DateLocaleConverter == null ? (class$org$apache$commons$beanutils$locale$converters$DateLocaleConverter = class$("org.apache.commons.beanutils.locale.converters.DateLocaleConverter")) : class$org$apache$commons$beanutils$locale$converters$DateLocaleConverter);
   }
}
