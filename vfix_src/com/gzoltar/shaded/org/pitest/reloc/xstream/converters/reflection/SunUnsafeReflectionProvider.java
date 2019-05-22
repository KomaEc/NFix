package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

public class SunUnsafeReflectionProvider extends SunLimitedUnsafeReflectionProvider {
   private transient Map fieldOffsetCache;

   public SunUnsafeReflectionProvider() {
   }

   public SunUnsafeReflectionProvider(FieldDictionary dic) {
      super(dic);
   }

   public void writeField(Object object, String fieldName, Object value, Class definedIn) {
      this.write(this.fieldDictionary.field(object.getClass(), fieldName, definedIn), object, value);
   }

   private void write(Field field, Object object, Object value) {
      if (exception != null) {
         throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), exception);
      } else {
         try {
            long offset = this.getFieldOffset(field);
            Class type = field.getType();
            if (type.isPrimitive()) {
               if (type.equals(Integer.TYPE)) {
                  unsafe.putInt(object, offset, (Integer)value);
               } else if (type.equals(Long.TYPE)) {
                  unsafe.putLong(object, offset, (Long)value);
               } else if (type.equals(Short.TYPE)) {
                  unsafe.putShort(object, offset, (Short)value);
               } else if (type.equals(Character.TYPE)) {
                  unsafe.putChar(object, offset, (Character)value);
               } else if (type.equals(Byte.TYPE)) {
                  unsafe.putByte(object, offset, (Byte)value);
               } else if (type.equals(Float.TYPE)) {
                  unsafe.putFloat(object, offset, (Float)value);
               } else if (type.equals(Double.TYPE)) {
                  unsafe.putDouble(object, offset, (Double)value);
               } else {
                  if (!type.equals(Boolean.TYPE)) {
                     throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName() + ": Unknown type " + type);
                  }

                  unsafe.putBoolean(object, offset, (Boolean)value);
               }
            } else {
               unsafe.putObject(object, offset, value);
            }

         } catch (IllegalArgumentException var8) {
            throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), var8);
         }
      }
   }

   private synchronized long getFieldOffset(Field f) {
      Long l = (Long)this.fieldOffsetCache.get(f);
      if (l == null) {
         l = new Long(unsafe.objectFieldOffset(f));
         this.fieldOffsetCache.put(f, l);
      }

      return l;
   }

   private Object readResolve() {
      this.init();
      return this;
   }

   protected void init() {
      super.init();
      this.fieldOffsetCache = new WeakHashMap();
   }
}
