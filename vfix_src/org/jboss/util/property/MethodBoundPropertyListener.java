package org.jboss.util.property;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jboss.util.ThrowableHandler;
import org.jboss.util.propertyeditor.PropertyEditors;

public class MethodBoundPropertyListener extends BoundPropertyAdapter {
   protected final String propertyName;
   protected final Object instance;
   protected final Method setter;
   protected final PropertyDescriptor descriptor;

   public MethodBoundPropertyListener(Object instance, String propertyName, String beanPropertyName) {
      this.instance = instance;
      this.propertyName = propertyName;

      try {
         this.descriptor = this.getPropertyDescriptor(beanPropertyName);
         if (this.descriptor == null) {
            throw new PropertyException("missing method for: " + beanPropertyName);
         } else {
            this.setter = this.descriptor.getWriteMethod();
            if (this.setter == null) {
               throw new PropertyException("missing setter method for: " + beanPropertyName);
            } else {
               try {
                  this.setter.setAccessible(true);
               } catch (SecurityException var5) {
                  ThrowableHandler.add(var5);
               }

            }
         }
      } catch (IntrospectionException var6) {
         throw new PropertyException(var6);
      }
   }

   private PropertyDescriptor getPropertyDescriptor(String beanPropertyName) throws IntrospectionException {
      Class<?> instanceType = this.instance.getClass();
      BeanInfo beanInfo = Introspector.getBeanInfo(instanceType);
      PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
      PropertyDescriptor descriptor = null;

      for(int i = 0; i < descriptors.length; ++i) {
         if (descriptors[i].getName().equals(beanPropertyName)) {
            descriptor = descriptors[i];
            break;
         }
      }

      return descriptor;
   }

   public MethodBoundPropertyListener(Object instance, String propertyName) {
      this(instance, propertyName, propertyName);
   }

   public final String getPropertyName() {
      return this.propertyName;
   }

   protected void invokeSetter(String value) {
      try {
         Class<?> type = this.descriptor.getPropertyType();
         PropertyEditor editor = PropertyEditors.findEditor(type);
         editor.setAsText(value);
         Object coerced = editor.getValue();
         this.setter.invoke(this.instance, coerced);
      } catch (InvocationTargetException var5) {
         Throwable target = var5.getTargetException();
         if (target instanceof PropertyException) {
            throw (PropertyException)target;
         } else {
            throw new PropertyException(target);
         }
      } catch (Exception var6) {
         throw new PropertyException(var6);
      }
   }

   public void propertyAdded(PropertyEvent event) {
      this.invokeSetter(event.getPropertyValue());
   }

   public void propertyChanged(PropertyEvent event) {
      this.invokeSetter(event.getPropertyValue());
   }

   public void propertyBound(PropertyMap map) {
      if (map.containsProperty(this.propertyName)) {
         this.invokeSetter(map.getProperty(this.propertyName));
      }

   }
}
