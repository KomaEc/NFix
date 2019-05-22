package org.codehaus.groovy.reflection;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaProperty;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class CachedField extends MetaProperty {
   public final Field field;

   public CachedField(Field field) {
      super(field.getName(), field.getType());
      this.field = field;
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public boolean isFinal() {
      return Modifier.isFinal(this.getModifiers());
   }

   public int getModifiers() {
      return this.field.getModifiers();
   }

   public Object getProperty(Object object) {
      try {
         return this.field.get(object);
      } catch (IllegalAccessException var3) {
         throw new GroovyRuntimeException("Cannot get the property '" + this.name + "'.", var3);
      }
   }

   public void setProperty(Object object, Object newValue) {
      Object goalValue = DefaultTypeTransformation.castToType(newValue, this.field.getType());
      if (this.isFinal()) {
         throw new GroovyRuntimeException("Cannot set the property '" + this.name + "' because the backing field is final.");
      } else {
         try {
            this.field.set(object, goalValue);
         } catch (IllegalAccessException var5) {
            throw new GroovyRuntimeException("Cannot set the property '" + this.name + "'.", var5);
         }
      }
   }
}
