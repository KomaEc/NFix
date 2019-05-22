package org.apache.commons.beanutils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LazyDynaBean implements DynaBean, Serializable {
   private static Log logger;
   protected static final BigInteger BigInteger_ZERO;
   protected static final BigDecimal BigDecimal_ZERO;
   protected static final Character Character_SPACE;
   protected static final Byte Byte_ZERO;
   protected static final Short Short_ZERO;
   protected static final Integer Integer_ZERO;
   protected static final Long Long_ZERO;
   protected static final Float Float_ZERO;
   protected static final Double Double_ZERO;
   protected Map values;
   protected MutableDynaClass dynaClass;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$LazyDynaBean;
   // $FF: synthetic field
   static Class class$java$util$List;
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$DynaBean;
   // $FF: synthetic field
   static Class class$java$lang$Number;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Character;
   // $FF: synthetic field
   static Class class$java$util$Date;
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

   public LazyDynaBean() {
      this((DynaClass)(new LazyDynaClass()));
   }

   public LazyDynaBean(String name) {
      this((DynaClass)(new LazyDynaClass(name)));
   }

   public LazyDynaBean(DynaClass dynaClass) {
      this.values = this.newMap();
      if (dynaClass instanceof MutableDynaClass) {
         this.dynaClass = (MutableDynaClass)dynaClass;
      } else {
         this.dynaClass = new LazyDynaClass(dynaClass.getName(), dynaClass.getDynaProperties());
      }

   }

   public Map getMap() {
      return this.values;
   }

   public int size(String name) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         Object value = this.values.get(name);
         if (value == null) {
            return 0;
         } else if (value instanceof Map) {
            return ((Map)value).size();
         } else if (value instanceof List) {
            return ((List)value).size();
         } else {
            return value.getClass().isArray() ? Array.getLength(value) : 0;
         }
      }
   }

   public boolean contains(String name, String key) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         Object value = this.values.get(name);
         if (value == null) {
            return false;
         } else {
            return value instanceof Map ? ((Map)value).containsKey(key) : false;
         }
      }
   }

   public Object get(String name) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         Object value = this.values.get(name);
         if (value != null) {
            return value;
         } else if (!this.isDynaProperty(name)) {
            return null;
         } else {
            value = this.createProperty(name, this.dynaClass.getDynaProperty(name).getType());
            if (value != null) {
               this.set(name, value);
            }

            return value;
         }
      }
   }

   public Object get(String name, int index) {
      if (!this.isDynaProperty(name)) {
         this.set(name, this.defaultIndexedProperty(name));
      }

      Object indexedProperty = this.get(name);
      if (!this.dynaClass.getDynaProperty(name).isIndexed()) {
         throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]' " + this.dynaClass.getDynaProperty(name).getName());
      } else {
         indexedProperty = this.growIndexedProperty(name, indexedProperty, index);
         if (indexedProperty.getClass().isArray()) {
            return Array.get(indexedProperty, index);
         } else if (indexedProperty instanceof List) {
            return ((List)indexedProperty).get(index);
         } else {
            throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]' " + indexedProperty.getClass().getName());
         }
      }
   }

   public Object get(String name, String key) {
      if (!this.isDynaProperty(name)) {
         this.set(name, this.defaultMappedProperty(name));
      }

      Object mappedProperty = this.get(name);
      if (!this.dynaClass.getDynaProperty(name).isMapped()) {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")' " + this.dynaClass.getDynaProperty(name).getType().getName());
      } else if (mappedProperty instanceof Map) {
         return ((Map)mappedProperty).get(key);
      } else {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'" + mappedProperty.getClass().getName());
      }
   }

   public DynaClass getDynaClass() {
      return this.dynaClass;
   }

   public void remove(String name, String key) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         Object value = this.values.get(name);
         if (value != null) {
            if (value instanceof Map) {
               ((Map)value).remove(key);
            } else {
               throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'" + value.getClass().getName());
            }
         }
      }
   }

   public void set(String name, Object value) {
      if (!this.isDynaProperty(name)) {
         if (this.dynaClass.isRestricted()) {
            throw new IllegalArgumentException("Invalid property name '" + name + "' (DynaClass is restricted)");
         }

         if (value == null) {
            this.dynaClass.add(name);
         } else {
            this.dynaClass.add(name, value.getClass());
         }
      }

      DynaProperty descriptor = this.dynaClass.getDynaProperty(name);
      if (value == null) {
         if (descriptor.getType().isPrimitive()) {
            throw new NullPointerException("Primitive value for '" + name + "'");
         }
      } else if (!this.isAssignable(descriptor.getType(), value.getClass())) {
         throw new ConversionException("Cannot assign value of type '" + value.getClass().getName() + "' to property '" + name + "' of type '" + descriptor.getType().getName() + "'");
      }

      this.values.put(name, value);
   }

   public void set(String name, int index, Object value) {
      if (!this.isDynaProperty(name)) {
         this.set(name, this.defaultIndexedProperty(name));
      }

      Object indexedProperty = this.get(name);
      if (!this.dynaClass.getDynaProperty(name).isIndexed()) {
         throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'" + this.dynaClass.getDynaProperty(name).getType().getName());
      } else {
         indexedProperty = this.growIndexedProperty(name, indexedProperty, index);
         if (indexedProperty.getClass().isArray()) {
            Array.set(indexedProperty, index, value);
         } else {
            if (!(indexedProperty instanceof List)) {
               throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]' " + indexedProperty.getClass().getName());
            }

            ((List)indexedProperty).set(index, value);
         }

      }
   }

   public void set(String name, String key, Object value) {
      if (!this.isDynaProperty(name)) {
         this.set(name, this.defaultMappedProperty(name));
      }

      Object mappedProperty = this.get(name);
      if (!this.dynaClass.getDynaProperty(name).isMapped()) {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'" + this.dynaClass.getDynaProperty(name).getType().getName());
      } else {
         ((Map)mappedProperty).put(key, value);
      }
   }

   protected Object growIndexedProperty(String name, Object indexedProperty, int index) {
      if (indexedProperty instanceof List) {
         List list = (List)indexedProperty;

         while(index >= list.size()) {
            list.add((Object)null);
         }
      }

      if (indexedProperty.getClass().isArray()) {
         int length = Array.getLength(indexedProperty);
         if (index >= length) {
            Class componentType = indexedProperty.getClass().getComponentType();
            Object newArray = Array.newInstance(componentType, index + 1);
            System.arraycopy(indexedProperty, 0, newArray, 0, length);
            indexedProperty = newArray;
            this.set(name, newArray);
            int newLength = Array.getLength(newArray);

            for(int i = length; i < newLength; ++i) {
               Array.set(indexedProperty, i, this.createProperty(name + "[" + i + "]", componentType));
            }
         }
      }

      return indexedProperty;
   }

   protected Object createProperty(String name, Class type) {
      if (!type.isArray() && !(class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List).isAssignableFrom(type)) {
         if ((class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(type)) {
            return this.createMappedProperty(name, type);
         } else if ((class$org$apache$commons$beanutils$DynaBean == null ? (class$org$apache$commons$beanutils$DynaBean = class$("org.apache.commons.beanutils.DynaBean")) : class$org$apache$commons$beanutils$DynaBean).isAssignableFrom(type)) {
            return this.createDynaBeanProperty(name, type);
         } else if (type.isPrimitive()) {
            return this.createPrimitiveProperty(name, type);
         } else {
            return (class$java$lang$Number == null ? (class$java$lang$Number = class$("java.lang.Number")) : class$java$lang$Number).isAssignableFrom(type) ? this.createNumberProperty(name, type) : this.createOtherProperty(name, type);
         }
      } else {
         return this.createIndexedProperty(name, type);
      }
   }

   protected Object createIndexedProperty(String name, Class type) {
      Object indexedProperty = null;
      if (type == null) {
         indexedProperty = this.defaultIndexedProperty(name);
      } else if (type.isArray()) {
         indexedProperty = Array.newInstance(type.getComponentType(), 0);
      } else {
         if (!(class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List).isAssignableFrom(type)) {
            throw new IllegalArgumentException("Non-indexed property of type '" + type.getName() + "' for '" + name + "'");
         }

         if (type.isInterface()) {
            indexedProperty = this.defaultIndexedProperty(name);
         } else {
            try {
               indexedProperty = type.newInstance();
            } catch (Exception var5) {
               throw new IllegalArgumentException("Error instantiating indexed property of type '" + type.getName() + "' for '" + name + "' " + var5);
            }
         }
      }

      return indexedProperty;
   }

   protected Object createMappedProperty(String name, Class type) {
      Object mappedProperty = null;
      if (type == null) {
         mappedProperty = this.defaultMappedProperty(name);
      } else if (type.isInterface()) {
         mappedProperty = this.defaultMappedProperty(name);
      } else {
         if (!(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(type)) {
            throw new IllegalArgumentException("Non-mapped property of type '" + type.getName() + "' for '" + name + "'");
         }

         try {
            mappedProperty = type.newInstance();
         } catch (Exception var5) {
            throw new IllegalArgumentException("Error instantiating mapped property of type '" + type.getName() + "' for '" + name + "' " + var5);
         }
      }

      return mappedProperty;
   }

   protected Object createDynaBeanProperty(String name, Class type) {
      try {
         return type.newInstance();
      } catch (Exception var4) {
         if (logger.isWarnEnabled()) {
            logger.warn("Error instantiating DynaBean property of type '" + type.getName() + "' for '" + name + "' " + var4);
         }

         return null;
      }
   }

   protected Object createPrimitiveProperty(String name, Class type) {
      if (type == Boolean.TYPE) {
         return Boolean.FALSE;
      } else if (type == Integer.TYPE) {
         return Integer_ZERO;
      } else if (type == Long.TYPE) {
         return Long_ZERO;
      } else if (type == Double.TYPE) {
         return Double_ZERO;
      } else if (type == Float.TYPE) {
         return Float_ZERO;
      } else if (type == Byte.TYPE) {
         return Byte_ZERO;
      } else if (type == Short.TYPE) {
         return Short_ZERO;
      } else {
         return type == Character.TYPE ? Character_SPACE : null;
      }
   }

   protected Object createNumberProperty(String name, Class type) {
      return null;
   }

   protected Object createOtherProperty(String name, Class type) {
      if (type != (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String) && type != (class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean) && type != (class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character) && !(class$java$util$Date == null ? (class$java$util$Date = class$("java.util.Date")) : class$java$util$Date).isAssignableFrom(type)) {
         try {
            return type.newInstance();
         } catch (Exception var4) {
            if (logger.isWarnEnabled()) {
               logger.warn("Error instantiating property of type '" + type.getName() + "' for '" + name + "' " + var4);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   protected Object defaultIndexedProperty(String name) {
      return new ArrayList();
   }

   protected Map defaultMappedProperty(String name) {
      return new HashMap();
   }

   protected boolean isDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else if (this.dynaClass instanceof LazyDynaClass) {
         return ((LazyDynaClass)this.dynaClass).isDynaProperty(name);
      } else {
         return this.dynaClass.getDynaProperty(name) != null;
      }
   }

   protected boolean isAssignable(Class dest, Class source) {
      return dest.isAssignableFrom(source) || dest == Boolean.TYPE && source == (class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean) || dest == Byte.TYPE && source == (class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte) || dest == Character.TYPE && source == (class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character) || dest == Double.TYPE && source == (class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double) || dest == Float.TYPE && source == (class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float) || dest == Integer.TYPE && source == (class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer) || dest == Long.TYPE && source == (class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long) || dest == Short.TYPE && source == (class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short);
   }

   protected Map newMap() {
      return new HashMap();
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
      logger = LogFactory.getLog(class$org$apache$commons$beanutils$LazyDynaBean == null ? (class$org$apache$commons$beanutils$LazyDynaBean = class$("org.apache.commons.beanutils.LazyDynaBean")) : class$org$apache$commons$beanutils$LazyDynaBean);
      BigInteger_ZERO = new BigInteger("0");
      BigDecimal_ZERO = new BigDecimal("0");
      Character_SPACE = new Character(' ');
      Byte_ZERO = new Byte((byte)0);
      Short_ZERO = new Short((short)0);
      Integer_ZERO = new Integer(0);
      Long_ZERO = new Long(0L);
      Float_ZERO = new Float(0.0F);
      Double_ZERO = new Double(0.0D);
   }
}
