package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class SunLimitedUnsafeReflectionProvider extends PureJavaReflectionProvider {
   protected static final Unsafe unsafe;
   protected static final Exception exception;

   public SunLimitedUnsafeReflectionProvider() {
   }

   public SunLimitedUnsafeReflectionProvider(FieldDictionary fieldDictionary) {
      super(fieldDictionary);
   }

   public Object newInstance(Class type) {
      if (exception != null) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), exception);
      } else {
         try {
            return unsafe.allocateInstance(type);
         } catch (SecurityException var3) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var3);
         } catch (InstantiationException var4) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var4);
         } catch (IllegalArgumentException var5) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var5);
         }
      }
   }

   protected void validateFieldAccess(Field field) {
   }

   private Object readResolve() {
      this.init();
      return this;
   }

   static {
      Unsafe u = null;
      Object ex = null;

      try {
         Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
         unsafeField.setAccessible(true);
         u = (Unsafe)unsafeField.get((Object)null);
      } catch (SecurityException var3) {
         ex = var3;
      } catch (NoSuchFieldException var4) {
         ex = var4;
      } catch (IllegalArgumentException var5) {
         ex = var5;
      } catch (IllegalAccessException var6) {
         ex = var6;
      }

      exception = (Exception)ex;
      unsafe = u;
   }
}
