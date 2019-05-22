package org.apache.commons.beanutils.locale.converters;

import java.util.Locale;

public class BigIntegerLocaleConverter extends DecimalLocaleConverter {
   public BigIntegerLocaleConverter() {
      this(false);
   }

   public BigIntegerLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public BigIntegerLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public BigIntegerLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public BigIntegerLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public BigIntegerLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public BigIntegerLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public BigIntegerLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public BigIntegerLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public BigIntegerLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public BigIntegerLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public BigIntegerLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern);
   }
}
