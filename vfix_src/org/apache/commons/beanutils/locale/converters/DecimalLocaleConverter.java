package org.apache.commons.beanutils.locale.converters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import org.apache.commons.beanutils.locale.BaseLocaleConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DecimalLocaleConverter extends BaseLocaleConverter {
   private static Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$converters$DecimalLocaleConverter;

   public DecimalLocaleConverter() {
      this(false);
   }

   public DecimalLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public DecimalLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public DecimalLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public DecimalLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public DecimalLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      this((Object)null, locale, pattern, locPattern);
   }

   public DecimalLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public DecimalLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public DecimalLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public DecimalLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public DecimalLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public DecimalLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern, locPattern);
   }

   protected Object parse(Object value, String pattern) throws ParseException {
      DecimalFormat formatter = (DecimalFormat)DecimalFormat.getInstance(this.locale);
      if (pattern != null) {
         if (this.locPattern) {
            formatter.applyLocalizedPattern(pattern);
         } else {
            formatter.applyPattern(pattern);
         }
      } else {
         log.warn("No pattern provided, using default.");
      }

      return formatter.parse((String)value);
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
      log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$converters$DecimalLocaleConverter == null ? (class$org$apache$commons$beanutils$locale$converters$DecimalLocaleConverter = class$("org.apache.commons.beanutils.locale.converters.DecimalLocaleConverter")) : class$org$apache$commons$beanutils$locale$converters$DecimalLocaleConverter);
   }
}
