package org.apache.commons.beanutils.locale.converters;

import java.sql.Date;
import java.text.ParseException;
import java.util.Locale;

public class SqlDateLocaleConverter extends DateLocaleConverter {
   public SqlDateLocaleConverter() {
      this(false);
   }

   public SqlDateLocaleConverter(boolean locPattern) {
      this(Locale.getDefault(), locPattern);
   }

   public SqlDateLocaleConverter(Locale locale) {
      this(locale, false);
   }

   public SqlDateLocaleConverter(Locale locale, boolean locPattern) {
      this(locale, (String)null, locPattern);
   }

   public SqlDateLocaleConverter(Locale locale, String pattern) {
      this(locale, pattern, false);
   }

   public SqlDateLocaleConverter(Locale locale, String pattern, boolean locPattern) {
      super(locale, pattern, locPattern);
   }

   public SqlDateLocaleConverter(Object defaultValue) {
      this(defaultValue, false);
   }

   public SqlDateLocaleConverter(Object defaultValue, boolean locPattern) {
      this(defaultValue, Locale.getDefault(), locPattern);
   }

   public SqlDateLocaleConverter(Object defaultValue, Locale locale) {
      this(defaultValue, locale, false);
   }

   public SqlDateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
      this(defaultValue, locale, (String)null, locPattern);
   }

   public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern) {
      this(defaultValue, locale, pattern, false);
   }

   public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
      super(defaultValue, locale, pattern, locPattern);
   }

   protected Object parse(Object value, String pattern) throws ParseException {
      return new Date(((java.util.Date)super.parse((String)value, pattern)).getTime());
   }
}
