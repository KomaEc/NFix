package org.apache.commons.beanutils.locale;

import java.text.ParseException;
import java.util.Locale;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseLocaleConverter implements LocaleConverter {
   private static Log log;
   private Object defaultValue;
   protected boolean useDefault;
   protected Locale locale;
   protected String pattern;
   protected boolean locPattern;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$BaseLocaleConverter;

   protected BaseLocaleConverter(Locale locale, String pattern) {
      this((Object)null, locale, pattern, false, false);
   }

   protected BaseLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      this((Object)null, locale, pattern, false, locPattern);
   }

   protected BaseLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   protected BaseLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      this(defaultValue, locale, pattern, true, locPattern);
   }

   private BaseLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean useDefault, boolean locPattern) {
      this.defaultValue = null;
      this.useDefault = false;
      this.locale = Locale.getDefault();
      this.pattern = null;
      this.locPattern = false;
      if (useDefault) {
         this.defaultValue = defaultValue;
         this.useDefault = true;
      }

      if (locale != null) {
         this.locale = locale;
      }

      this.pattern = pattern;
      this.locPattern = locPattern;
   }

   protected abstract Object parse(Object var1, String var2) throws ParseException;

   public Object convert(Object value) {
      return this.convert((Object)value, (String)null);
   }

   public Object convert(Object value, String pattern) {
      return this.convert((Class)null, value, pattern);
   }

   public Object convert(Class type, Object value) {
      return this.convert(type, value, (String)null);
   }

   public Object convert(Class type, Object value, String pattern) {
      if (value == null) {
         if (this.useDefault) {
            return this.defaultValue;
         } else {
            log.debug("Null value specified for conversion, returing null");
            return null;
         }
      } else {
         try {
            return pattern != null ? this.parse(value, pattern) : this.parse(value, this.pattern);
         } catch (Exception var5) {
            if (this.useDefault) {
               return this.defaultValue;
            } else {
               throw new ConversionException(var5);
            }
         }
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
      log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$BaseLocaleConverter == null ? (class$org$apache$commons$beanutils$locale$BaseLocaleConverter = class$("org.apache.commons.beanutils.locale.BaseLocaleConverter")) : class$org$apache$commons$beanutils$locale$BaseLocaleConverter);
   }
}
