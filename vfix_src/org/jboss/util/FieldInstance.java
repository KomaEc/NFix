package org.jboss.util;

import java.lang.reflect.Field;

public class FieldInstance {
   protected final Field field;
   protected final Object instance;

   public FieldInstance(Object instance, String fieldName) throws NoSuchFieldException {
      if (instance == null) {
         throw new NullArgumentException("instance");
      } else if (fieldName == null) {
         throw new NullArgumentException("fieldName");
      } else {
         this.field = instance.getClass().getField(fieldName);
         if (!this.field.getDeclaringClass().isAssignableFrom(instance.getClass())) {
            throw new IllegalArgumentException("field does not belong to instance class");
         } else {
            this.instance = instance;
         }
      }
   }

   public final Field getField() {
      return this.field;
   }

   public final Object getInstance() {
      return this.instance;
   }

   public final Object get() throws IllegalAccessException {
      return this.field.get(this.instance);
   }

   public final void set(Object value) throws IllegalAccessException {
      this.field.set(this.instance, value);
   }
}
