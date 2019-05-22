package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Fields {
   public static Field locate(Class definedIn, Class fieldType, boolean isStatic) {
      Field field = null;

      try {
         Field[] fields = definedIn.getDeclaredFields();

         for(int i = 0; i < fields.length; ++i) {
            if (Modifier.isStatic(fields[i].getModifiers()) == isStatic && fieldType.isAssignableFrom(fields[i].getType())) {
               field = fields[i];
            }
         }

         if (field != null && !field.isAccessible()) {
            field.setAccessible(true);
         }
      } catch (SecurityException var6) {
      } catch (NoClassDefFoundError var7) {
      }

      return field;
   }

   public static Field find(Class type, String name) {
      try {
         Field result = type.getDeclaredField(name);
         if (!result.isAccessible()) {
            result.setAccessible(true);
         }

         return result;
      } catch (NoSuchFieldException var3) {
         throw new IllegalArgumentException("Could not access " + type.getName() + "." + name + " field: " + var3.getMessage());
      } catch (NoClassDefFoundError var4) {
         throw new ObjectAccessException("Could not access " + type.getName() + "." + name + " field: " + var4.getMessage());
      }
   }

   public static void write(Field field, Object instance, Object value) {
      try {
         field.set(instance, value);
      } catch (IllegalAccessException var4) {
         throw new ObjectAccessException("Could not write " + field.getType().getName() + "." + field.getName() + " field", var4);
      } catch (NoClassDefFoundError var5) {
         throw new ObjectAccessException("Could not write " + field.getType().getName() + "." + field.getName() + " field", var5);
      }
   }

   public static Object read(Field field, Object instance) {
      try {
         return field.get(instance);
      } catch (IllegalAccessException var3) {
         throw new ObjectAccessException("Could not read " + field.getType().getName() + "." + field.getName() + " field", var3);
      } catch (NoClassDefFoundError var4) {
         throw new ObjectAccessException("Could not read " + field.getType().getName() + "." + field.getName() + " field", var4);
      }
   }
}
