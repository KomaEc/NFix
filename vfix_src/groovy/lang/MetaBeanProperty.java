package groovy.lang;

import java.lang.reflect.Modifier;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MetaBeanProperty extends MetaProperty {
   private MetaMethod getter;
   private MetaMethod setter;
   private CachedField field;

   public MetaBeanProperty(String name, Class type, MetaMethod getter, MetaMethod setter) {
      super(name, type);
      this.getter = getter;
      this.setter = setter;
   }

   public Object getProperty(Object object) {
      MetaMethod getter = this.getGetter();
      if (getter == null) {
         if (this.field != null) {
            return this.field.getProperty(object);
         } else {
            throw new GroovyRuntimeException("Cannot read write-only property: " + this.name);
         }
      } else {
         return getter.invoke(object, MetaClassHelper.EMPTY_ARRAY);
      }
   }

   public void setProperty(Object object, Object newValue) {
      MetaMethod setter = this.getSetter();
      if (setter == null) {
         if (this.field != null && !Modifier.isFinal(this.field.getModifiers())) {
            this.field.setProperty(object, newValue);
         } else {
            throw new GroovyRuntimeException("Cannot set read-only property: " + this.name);
         }
      } else {
         newValue = DefaultTypeTransformation.castToType(newValue, this.getType());
         setter.invoke(object, new Object[]{newValue});
      }
   }

   public MetaMethod getGetter() {
      return this.getter;
   }

   public MetaMethod getSetter() {
      return this.setter;
   }

   void setGetter(MetaMethod getter) {
      this.getter = getter;
   }

   void setSetter(MetaMethod setter) {
      this.setter = setter;
   }

   public int getModifiers() {
      MetaMethod getter = this.getGetter();
      MetaMethod setter = this.getSetter();
      if (setter != null && getter == null) {
         return setter.getModifiers();
      } else if (getter != null && setter == null) {
         return getter.getModifiers();
      } else {
         int modifiers = getter.getModifiers() | setter.getModifiers();
         int visibility = 0;
         if (Modifier.isPublic(modifiers)) {
            visibility = 1;
         }

         if (Modifier.isProtected(modifiers)) {
            visibility = 4;
         }

         if (Modifier.isPrivate(modifiers)) {
            visibility = 2;
         }

         int states = getter.getModifiers() & setter.getModifiers();
         states &= -8;
         states |= visibility;
         return states;
      }
   }

   public void setField(CachedField f) {
      this.field = f;
   }

   public CachedField getField() {
      return this.field;
   }
}
