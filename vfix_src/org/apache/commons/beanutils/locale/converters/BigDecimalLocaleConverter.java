package org.apache.commons.beanutils.locale.converters;

import java.util.Locale;

public class BigDecimalLocaleConverter extends DecimalLocaleConverter {
   public BigDecimalLocaleConverter() {
      this(false);
   }

   public BigDecimalLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public BigDecimalLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public BigDecimalLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public BigDecimalLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public BigDecimalLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public BigDecimalLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public BigDecimalLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public BigDecimalLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public BigDecimalLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public BigDecimalLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public BigDecimalLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern);
   }
}
