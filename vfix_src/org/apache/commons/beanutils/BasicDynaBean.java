package org.apache.commons.beanutils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDynaBean implements DynaBean, Serializable {
   protected DynaClass dynaClass = null;
   protected HashMap values = new HashMap();
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

   public BasicDynaBean(DynaClass dynaClass) {
      this.dynaClass = dynaClass;
   }

   public boolean contains(String name, String key) {
      Object value = this.values.get(name);
      if (value == null) {
         throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
      } else if (value instanceof Map) {
         return ((Map)value).containsKey(key);
      } else {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
      }
   }

   public Object get(String name) {
      Object value = this.values.get(name);
      if (value != null) {
         return value;
      } else {
         Class type = this.getDynaProperty(name).getType();
         if (!type.isPrimitive()) {
            return value;
         } else if (type == Boolean.TYPE) {
            return Boolean.FALSE;
         } else if (type == Byte.TYPE) {
            return new Byte((byte)0);
         } else if (type == Character.TYPE) {
            return new Character('\u0000');
         } else if (type == Double.TYPE) {
            return new Double(0.0D);
         } else if (type == Float.TYPE) {
            return new Float(0.0F);
         } else if (type == Integer.TYPE) {
            return new Integer(0);
         } else if (type == Long.TYPE) {
            return new Long(0L);
         } else {
            return type == Short.TYPE ? new Short((short)0) : null;
         }
      }
   }

   public Object get(String name, int index) {
      Object value = this.values.get(name);
      if (value == null) {
         throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
      } else if (value.getClass().isArray()) {
         return Array.get(value, index);
      } else if (value instanceof List) {
         return ((List)value).get(index);
      } else {
         throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
      }
   }

   public Object get(String name, String key) {
      Object value = this.values.get(name);
      if (value == null) {
         throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
      } else if (value instanceof Map) {
         return ((Map)value).get(key);
      } else {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
      }
   }

   public DynaClass getDynaClass() {
      return this.dynaClass;
   }

   public void remove(String name, String key) {
      Object value = this.values.get(name);
      if (value == null) {
         throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
      } else if (value instanceof Map) {
         ((Map)value).remove(key);
      } else {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
      }
   }

   public void set(String name, Object value) {
      DynaProperty descriptor = this.getDynaProperty(name);
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
      Object prop = this.values.get(name);
      if (prop == null) {
         throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
      } else {
         if (prop.getClass().isArray()) {
            Array.set(prop, index, value);
         } else {
            if (!(prop instanceof List)) {
               throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
            }

            try {
               ((List)prop).set(index, value);
            } catch (ClassCastException var6) {
               throw new ConversionException(var6.getMessage());
            }
         }

      }
   }

   public void set(String name, String key, Object value) {
      Object prop = this.values.get(name);
      if (prop == null) {
         throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
      } else if (prop instanceof Map) {
         ((Map)prop).put(key, value);
      } else {
         throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
      }
   }

   protected DynaProperty getDynaProperty(String name) {
      DynaProperty descriptor = this.getDynaClass().getDynaProperty(name);
      if (descriptor == null) {
         throw new IllegalArgumentException("Invalid property name '" + name + "'");
      } else {
         return descriptor;
      }
   }

   protected boolean isAssignable(Class dest, Class source) {
      return dest.isAssignableFrom(source) || dest == Boolean.TYPE && source == (class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean) || dest == Byte.TYPE && source == (class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte) || dest == Character.TYPE && source == (class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character) || dest == Double.TYPE && source == (class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double) || dest == Float.TYPE && source == (class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float) || dest == Integer.TYPE && source == (class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer) || dest == Long.TYPE && source == (class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long) || dest == Short.TYPE && source == (class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short);
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
