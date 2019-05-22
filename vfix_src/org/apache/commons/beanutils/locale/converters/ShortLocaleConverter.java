package org.apache.commons.beanutils.locale.converters;

import java.util.Locale;

public class ShortLocaleConverter extends DecimalLocaleConverter {
   public ShortLocaleConverter() {
      this(false);
   }

   public ShortLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public ShortLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public ShortLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public ShortLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public ShortLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public ShortLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public ShortLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public ShortLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public ShortLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public ShortLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public ShortLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern);
   }
}
