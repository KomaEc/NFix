package org.apache.commons.beanutils.locale.converters;

import java.util.Locale;

public class LongLocaleConverter extends DecimalLocaleConverter {
   public LongLocaleConverter() {
      this(false);
   }

   public LongLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public LongLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public LongLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public LongLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public LongLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public LongLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public LongLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public LongLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public LongLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public LongLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public LongLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern, locPattern);
   }
}
