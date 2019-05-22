package org.apache.commons.beanutils;

import java.lang.reflect.Array;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteArrayConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterArrayConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleArrayConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.FloatArrayConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongArrayConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortArrayConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConvertUtilsBean {
   private FastHashMap converters = new FastHashMap();
   private Log log;
   /** @deprecated */
   private Boolean defaultBoolean;
   /** @deprecated */
   private Byte defaultByte;
   /** @deprecated */
   private Character defaultCharacter;
   /** @deprecated */
   private Double defaultDouble;
   /** @deprecated */
   private Float defaultFloat;
   /** @deprecated */
   private Integer defaultInteger;
   /** @deprecated */
   private Long defaultLong;
   /** @deprecated */
   private static Short defaultShort = new Short((short)0);
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$ConvertUtils;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Character;
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
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$math$BigDecimal;
   // $FF: synthetic field
   static Class class$java$math$BigInteger;
   // $FF: synthetic field
   static Class class$java$lang$Class;
   // $FF: synthetic field
   static Class class$java$sql$Date;
   // $FF: synthetic field
   static Class class$java$sql$Time;
   // $FF: synthetic field
   static Class class$java$sql$Timestamp;
   // $FF: synthetic field
   static Class class$java$io$File;
   // $FF: synthetic field
   static Class class$java$net$URL;

   protected static ConvertUtilsBean getInstance() {
      return BeanUtilsBean.getInstance().getConvertUtils();
   }

   public ConvertUtilsBean() {
      this.log = LogFactory.getLog(class$org$apache$commons$beanutils$ConvertUtils == null ? (class$org$apache$commons$beanutils$ConvertUtils = class$("org.apache.commons.beanutils.ConvertUtils")) : class$org$apache$commons$beanutils$ConvertUtils);
      this.defaultBoolean = Boolean.FALSE;
      this.defaultByte = new Byte((byte)0);
      this.defaultCharacter = new Character(' ');
      this.defaultDouble = new Double(0.0D);
      this.defaultFloat = new Float(0.0F);
      this.defaultInteger = new Integer(0);
      this.defaultLong = new Long(0L);
      this.converters.setFast(false);
      this.deregister();
      this.converters.setFast(true);
   }

   /** @deprecated */
   public boolean getDefaultBoolean() {
      return this.defaultBoolean;
   }

   /** @deprecated */
   public void setDefaultBoolean(boolean newDefaultBoolean) {
      this.defaultBoolean = new Boolean(newDefaultBoolean);
      this.register((Converter)(new BooleanConverter(this.defaultBoolean)), (Class)Boolean.TYPE);
      this.register((Converter)(new BooleanConverter(this.defaultBoolean)), (Class)(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean));
   }

   /** @deprecated */
   public byte getDefaultByte() {
      return this.defaultByte;
   }

   /** @deprecated */
   public void setDefaultByte(byte newDefaultByte) {
      this.defaultByte = new Byte(newDefaultByte);
      this.register((Converter)(new ByteConverter(this.defaultByte)), (Class)Byte.TYPE);
      this.register((Converter)(new ByteConverter(this.defaultByte)), (Class)(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte));
   }

   /** @deprecated */
   public char getDefaultCharacter() {
      return this.defaultCharacter;
   }

   /** @deprecated */
   public void setDefaultCharacter(char newDefaultCharacter) {
      this.defaultCharacter = new Character(newDefaultCharacter);
      this.register((Converter)(new CharacterConverter(this.defaultCharacter)), (Class)Character.TYPE);
      this.register((Converter)(new CharacterConverter(this.defaultCharacter)), (Class)(class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character));
   }

   /** @deprecated */
   public double getDefaultDouble() {
      return this.defaultDouble;
   }

   /** @deprecated */
   public void setDefaultDouble(double newDefaultDouble) {
      this.defaultDouble = new Double(newDefaultDouble);
      this.register((Converter)(new DoubleConverter(this.defaultDouble)), (Class)Double.TYPE);
      this.register((Converter)(new DoubleConverter(this.defaultDouble)), (Class)(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double));
   }

   /** @deprecated */
   public float getDefaultFloat() {
      return this.defaultFloat;
   }

   /** @deprecated */
   public void setDefaultFloat(float newDefaultFloat) {
      this.defaultFloat = new Float(newDefaultFloat);
      this.register((Converter)(new FloatConverter(this.defaultFloat)), (Class)Float.TYPE);
      this.register((Converter)(new FloatConverter(this.defaultFloat)), (Class)(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float));
   }

   /** @deprecated */
   public int getDefaultInteger() {
      return this.defaultInteger;
   }

   /** @deprecated */
   public void setDefaultInteger(int newDefaultInteger) {
      this.defaultInteger = new Integer(newDefaultInteger);
      this.register((Converter)(new IntegerConverter(this.defaultInteger)), (Class)Integer.TYPE);
      this.register((Converter)(new IntegerConverter(this.defaultInteger)), (Class)(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer));
   }

   /** @deprecated */
   public long getDefaultLong() {
      return this.defaultLong;
   }

   /** @deprecated */
   public void setDefaultLong(long newDefaultLong) {
      this.defaultLong = new Long(newDefaultLong);
      this.register((Converter)(new LongConverter(this.defaultLong)), (Class)Long.TYPE);
      this.register((Converter)(new LongConverter(this.defaultLong)), (Class)(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long));
   }

   /** @deprecated */
   public short getDefaultShort() {
      return defaultShort;
   }

   /** @deprecated */
   public void setDefaultShort(short newDefaultShort) {
      defaultShort = new Short(newDefaultShort);
      this.register((Converter)(new ShortConverter(defaultShort)), (Class)Short.TYPE);
      this.register((Converter)(new ShortConverter(defaultShort)), (Class)(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short));
   }

   public String convert(Object value) {
      if (value == null) {
         return (String)null;
      } else {
         Converter converter;
         if (value.getClass().isArray()) {
            if (Array.getLength(value) < 1) {
               return null;
            } else {
               value = Array.get(value, 0);
               if (value == null) {
                  return (String)null;
               } else {
                  converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
                  return (String)converter.convert(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, value);
               }
            }
         } else {
            converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return (String)converter.convert(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, value);
         }
      }
   }

   public Object convert(String value, Class clazz) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Convert string '" + value + "' to class '" + clazz.getName() + "'");
      }

      Converter converter = this.lookup(clazz);
      if (converter == null) {
         converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("  Using converter " + converter);
      }

      return converter.convert(clazz, value);
   }

   public Object convert(String[] values, Class clazz) {
      Class type = clazz;
      if (clazz.isArray()) {
         type = clazz.getComponentType();
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("Convert String[" + values.length + "] to class '" + type.getName() + "[]'");
      }

      Converter converter = this.lookup(type);
      if (converter == null) {
         converter = this.lookup(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("  Using converter " + converter);
      }

      Object array = Array.newInstance(type, values.length);

      for(int i = 0; i < values.length; ++i) {
         Array.set(array, i, converter.convert(type, values[i]));
      }

      return array;
   }

   public void deregister() {
      boolean[] booleanArray = new boolean[0];
      byte[] byteArray = new byte[0];
      char[] charArray = new char[0];
      double[] doubleArray = new double[0];
      float[] floatArray = new float[0];
      int[] intArray = new int[0];
      long[] longArray = new long[0];
      short[] shortArray = new short[0];
      String[] stringArray = new String[0];
      this.converters.clear();
      this.register((Class)(class$java$math$BigDecimal == null ? (class$java$math$BigDecimal = class$("java.math.BigDecimal")) : class$java$math$BigDecimal), (Converter)(new BigDecimalConverter()));
      this.register((Class)(class$java$math$BigInteger == null ? (class$java$math$BigInteger = class$("java.math.BigInteger")) : class$java$math$BigInteger), (Converter)(new BigIntegerConverter()));
      this.register((Class)Boolean.TYPE, (Converter)(new BooleanConverter(this.defaultBoolean)));
      this.register((Class)(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean), (Converter)(new BooleanConverter(this.defaultBoolean)));
      this.register((Class)booleanArray.getClass(), (Converter)(new BooleanArrayConverter(booleanArray)));
      this.register((Class)Byte.TYPE, (Converter)(new ByteConverter(this.defaultByte)));
      this.register((Class)(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte), (Converter)(new ByteConverter(this.defaultByte)));
      this.register((Class)byteArray.getClass(), (Converter)(new ByteArrayConverter(byteArray)));
      this.register((Class)Character.TYPE, (Converter)(new CharacterConverter(this.defaultCharacter)));
      this.register((Class)(class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character), (Converter)(new CharacterConverter(this.defaultCharacter)));
      this.register((Class)charArray.getClass(), (Converter)(new CharacterArrayConverter(charArray)));
      this.register((Class)(class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class), (Converter)(new ClassConverter()));
      this.register((Class)Double.TYPE, (Converter)(new DoubleConverter(this.defaultDouble)));
      this.register((Class)(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double), (Converter)(new DoubleConverter(this.defaultDouble)));
      this.register((Class)doubleArray.getClass(), (Converter)(new DoubleArrayConverter(doubleArray)));
      this.register((Class)Float.TYPE, (Converter)(new FloatConverter(this.defaultFloat)));
      this.register((Class)(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float), (Converter)(new FloatConverter(this.defaultFloat)));
      this.register((Class)floatArray.getClass(), (Converter)(new FloatArrayConverter(floatArray)));
      this.register((Class)Integer.TYPE, (Converter)(new IntegerConverter(this.defaultInteger)));
      this.register((Class)(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer), (Converter)(new IntegerConverter(this.defaultInteger)));
      this.register((Class)intArray.getClass(), (Converter)(new IntegerArrayConverter(intArray)));
      this.register((Class)Long.TYPE, (Converter)(new LongConverter(this.defaultLong)));
      this.register((Class)(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long), (Converter)(new LongConverter(this.defaultLong)));
      this.register((Class)longArray.getClass(), (Converter)(new LongArrayConverter(longArray)));
      this.register((Class)Short.TYPE, (Converter)(new ShortConverter(defaultShort)));
      this.register((Class)(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short), (Converter)(new ShortConverter(defaultShort)));
      this.register((Class)shortArray.getClass(), (Converter)(new ShortArrayConverter(shortArray)));
      this.register((Class)(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String), (Converter)(new StringConverter()));
      this.register((Class)stringArray.getClass(), (Converter)(new StringArrayConverter(stringArray)));
      this.register((Class)(class$java$sql$Date == null ? (class$java$sql$Date = class$("java.sql.Date")) : class$java$sql$Date), (Converter)(new SqlDateConverter()));
      this.register((Class)(class$java$sql$Time == null ? (class$java$sql$Time = class$("java.sql.Time")) : class$java$sql$Time), (Converter)(new SqlTimeConverter()));
      this.register((Class)(class$java$sql$Timestamp == null ? (class$java$sql$Timestamp = class$("java.sql.Timestamp")) : class$java$sql$Timestamp), (Converter)(new SqlTimestampConverter()));
      this.register((Class)(class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File), (Converter)(new FileConverter()));
      this.register((Class)(class$java$net$URL == null ? (class$java$net$URL = class$("java.net.URL")) : class$java$net$URL), (Converter)(new URLConverter()));
   }

   private void register(Class clazz, Converter converter) {
      this.register(converter, clazz);
   }

   public void deregister(Class clazz) {
      this.converters.remove(clazz);
   }

   public Converter lookup(Class clazz) {
      return (Converter)this.converters.get(clazz);
   }

   public void register(Converter converter, Class clazz) {
      this.converters.put(clazz, converter);
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
