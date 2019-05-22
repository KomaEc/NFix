package org.apache.commons.beanutils.locale.converters;

import java.text.ParseException;
import java.util.Locale;
import org.apache.commons.beanutils.ConversionException;

public class FloatLocaleConverter extends DecimalLocaleConverter {
   public FloatLocaleConverter() {
      this(false);
   }

   public FloatLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public FloatLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public FloatLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public FloatLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public FloatLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public FloatLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public FloatLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public FloatLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public FloatLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public FloatLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public FloatLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern);
   }

   protected Object parse(Object value, String pattern) throws ParseException {
      Number parsed = (Number)super.parse(value, pattern);
      if (Math.abs(parsed.doubleValue() - (double)parsed.floatValue()) > (double)parsed.floatValue() * 1.0E-5D) {
         throw new ConversionException("Suplied number is not of type Float: " + parsed.longValue());
      } else {
         return new Float(parsed.floatValue());
      }
   }
}
