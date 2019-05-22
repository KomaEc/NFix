package org.apache.commons.beanutils.locale;

import java.lang.reflect.Array;
import java.util.Locale;
import org.apache.commons.beanutils.locale.converters.BigDecimalLocaleConverter;
import org.apache.commons.beanutils.locale.converters.BigIntegerLocaleConverter;
import org.apache.commons.beanutils.locale.converters.ByteLocaleConverter;
import org.apache.commons.beanutils.locale.converters.DoubleLocaleConverter;
import org.apache.commons.beanutils.locale.converters.FloatLocaleConverter;
import org.apache.commons.beanutils.locale.converters.IntegerLocaleConverter;
import org.apache.commons.beanutils.locale.converters.LongLocaleConverter;
import org.apache.commons.beanutils.locale.converters.ShortLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlDateLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimeLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimestampLocaleConverter;
import org.apache.commons.beanutils.locale.converters.StringLocaleConverter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LocaleConvertUtilsBean {
   private Locale defaultLocale = Locale.getDefault();
   private boolean applyLocalized = false;
   private Log log;
   private FastHashMap mapConverters;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$LocaleConvertUtils;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$math$BigDecimal;
   // $FF: synthetic field
   static Class class$java$math$BigInteger;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$sql$Date;
   // $FF: synthetic field
   static Class class$java$sql$Time;
   // $FF: synthetic field
   static Class class$java$sql$Timestamp;

   public static LocaleConvertUtilsBean getInstance() {
      return LocaleBeanUtilsBean.getLocaleBeanUtilsInstance().getLocaleConvertUtils();
   }

   public LocaleConvertUtilsBean() {
      this.log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$LocaleConvertUtils == null ? (class$org$apache$commons$beanutils$locale$LocaleConvertUtils = class$("org.apache.commons.beanutils.locale.LocaleConvertUtils")) : class$org$apache$commons$beanutils$locale$LocaleConvertUtils);
      this.mapConverters = new FastHashMap();
      this.deregister();
   }

   public Locale getDefaultLocale() {
      return this.defaultLocale;
   }

   public void setDefaultLocale(Locale locale) {
      if (locale == null) {
         this.defaultLocale = Locale.getDefault();
      } else {
         this.defaultLocale = locale;
      }

   }

   public boolean getApplyLocalized() {
      return this.applyLocalized;
   }

   public void setApplyLocalized(boolean newApplyLocalized) {
      this.applyLocalized = newApplyLocalized;
   }

   public String convert(Object value) {
      return this.convert((Object)value, (Locale)this.defaultLocale, (String)null);
   }

   public String convert(Object value, String pattern) {
      return this.convert(value, this.defaultLocale, pattern);
   }

   public String convert(Object value, Locale locale, String pattern) {
      LocaleConverter converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, locale);
      return (String)converter.convert(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, value, pattern);
   }

   public Object convert(String value, Class clazz) {
      return this.convert((String)value, clazz, this.defaultLocale, (String)null);
   }

   public Object convert(String value, Class clazz, String pattern) {
      return this.convert(value, clazz, this.defaultLocale, pattern);
   }

   public Object convert(String value, Class clazz, Locale locale, String pattern) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Convert string " + value + " to class " + clazz.getName() + " using " + locale.toString() + " locale and " + pattern + " pattern");
      }

      LocaleConverter converter = this.lookup(clazz, locale);
      if (converter == null) {
         converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, locale);
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("  Using converter " + converter);
      }

      return converter.convert(clazz, value, pattern);
   }

   public Object convert(String[] values, Class clazz, String pattern) {
      return this.convert(values, clazz, this.getDefaultLocale(), pattern);
   }

   public Object convert(String[] values, Class clazz) {
      return this.convert((String[])values, clazz, this.getDefaultLocale(), (String)null);
   }

   public Object convert(String[] values, Class clazz, Locale locale, String pattern) {
      Class type = clazz;
      if (clazz.isArray()) {
         type = clazz.getComponentType();
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("Convert String[" + values.length + "] to class " + type.getName() + "[] using " + locale.toString() + " locale and " + pattern + " pattern");
      }

      Object array = Array.newInstance(type, values.length);

      for(int i = 0; i < values.length; ++i) {
         Array.set(array, i, this.convert(values[i], type, locale, pattern));
      }

      return array;
   }

   public void register(LocaleConverter converter, Class clazz, Locale locale) {
      this.lookup(locale).put(clazz, converter);
   }

   public void deregister() {
      FastHashMap defaultConverter = this.lookup(this.defaultLocale);
      this.mapConverters.setFast(false);
      this.mapConverters.clear();
      this.mapConverters.put(this.defaultLocale, defaultConverter);
      this.mapConverters.setFast(true);
   }

   public void deregister(Locale locale) {
      this.mapConverters.remove(locale);
   }

   public void deregister(Class clazz, Locale locale) {
      this.lookup(locale).remove(clazz);
   }

   public LocaleConverter lookup(Class clazz, Locale locale) {
      LocaleConverter converter = (LocaleConverter)this.lookup(locale).get(clazz);
      if (this.log.isTraceEnabled()) {
         this.log.trace("LocaleConverter:" + converter);
      }

      return converter;
   }

   protected FastHashMap lookup(Locale locale) {
      FastHashMap localeConverters;
      if (locale == null) {
         localeConverters = (FastHashMap)this.mapConverters.get(this.defaultLocale);
      } else {
         localeConverters = (FastHashMap)this.mapConverters.get(locale);
         if (localeConverters == null) {
            localeConverters = this.create(locale);
            this.mapConverters.put(locale, localeConverters);
         }
      }

      return localeConverters;
   }

   protected FastHashMap create(Locale locale) {
      FastHashMap converter = new FastHashMap();
      converter.setFast(false);
      converter.put(class$java$math$BigDecimal == null ? (class$java$math$BigDecimal = class$("java.math.BigDecimal")) : class$java$math$BigDecimal, new BigDecimalLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$math$BigInteger == null ? (class$java$math$BigInteger = class$("java.math.BigInteger")) : class$java$math$BigInteger, new BigIntegerLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte, new ByteLocaleConverter(locale, this.applyLocalized));
      converter.put(Byte.TYPE, new ByteLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, new DoubleLocaleConverter(locale, this.applyLocalized));
      converter.put(Double.TYPE, new DoubleLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float, new FloatLocaleConverter(locale, this.applyLocalized));
      converter.put(Float.TYPE, new FloatLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer, new IntegerLocaleConverter(locale, this.applyLocalized));
      converter.put(Integer.TYPE, new IntegerLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long, new LongLocaleConverter(locale, this.applyLocalized));
      converter.put(Long.TYPE, new LongLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short, new ShortLocaleConverter(locale, this.applyLocalized));
      converter.put(Short.TYPE, new ShortLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, new StringLocaleConverter(locale, this.applyLocalized));
      converter.put(class$java$sql$Date == null ? (class$java$sql$Date = class$("java.sql.Date")) : class$java$sql$Date, new SqlDateLocaleConverter(locale, "yyyy-MM-dd"));
      converter.put(class$java$sql$Time == null ? (class$java$sql$Time = class$("java.sql.Time")) : class$java$sql$Time, new SqlTimeLocaleConverter(locale, "HH:mm:ss"));
      converter.put(class$java$sql$Timestamp == null ? (class$java$sql$Timestamp = class$("java.sql.Timestamp")) : class$java$sql$Timestamp, new SqlTimestampLocaleConverter(locale, "yyyy-MM-dd HH:mm:ss.S"));
      converter.setFast(true);
      return converter;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
