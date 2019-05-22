package org.codehaus.groovy.binding;

import groovy.lang.MissingMethodException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class PropertyBinding implements SourceBinding, TargetBinding, TriggerBinding {
   Object bean;
   String propertyName;
   boolean nonChangeCheck;

   public PropertyBinding(Object bean, String propertyName) {
      this.bean = bean;
      this.propertyName = propertyName;
   }

   public void updateTargetValue(Object newValue) {
      if (!this.nonChangeCheck || !DefaultTypeTransformation.compareEqual(this.getSourceValue(), newValue)) {
         try {
            InvokerHelper.setProperty(this.bean, this.propertyName, newValue);
         } catch (InvokerInvocationException var3) {
            if (!(var3.getCause() instanceof PropertyVetoException)) {
               throw var3;
            }
         }

      }
   }

   public boolean isNonChangeCheck() {
      return this.nonChangeCheck;
   }

   public void setNonChangeCheck(boolean nonChangeCheck) {
      this.nonChangeCheck = nonChangeCheck;
   }

   public Object getSourceValue() {
      return InvokerHelper.getPropertySafe(this.bean, this.propertyName);
   }

   public FullBinding createBinding(SourceBinding source, TargetBinding target) {
      return new PropertyBinding.PropertyFullBinding(source, target);
   }

   public Object getBean() {
      return this.bean;
   }

   public void setBean(Object bean) {
      this.bean = bean;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   class PropertyFullBinding extends AbstractFullBinding implements PropertyChangeListener {
      Object boundBean;
      Object boundProperty;
      boolean bound;
      boolean boundToProperty;

      PropertyFullBinding(SourceBinding source, TargetBinding target) {
         this.setSourceBinding(source);
         this.setTargetBinding(target);
      }

      public void propertyChange(PropertyChangeEvent event) {
         if (this.boundToProperty || event.getPropertyName().equals(this.boundProperty)) {
            this.update();
         }

      }

      public void bind() {
         if (!this.bound) {
            this.bound = true;
            this.boundBean = PropertyBinding.this.bean;
            this.boundProperty = PropertyBinding.this.propertyName;

            try {
               InvokerHelper.invokeMethodSafe(this.boundBean, "addPropertyChangeListener", new Object[]{this.boundProperty, this});
               this.boundToProperty = true;
            } catch (MissingMethodException var4) {
               try {
                  this.boundToProperty = false;
                  InvokerHelper.invokeMethodSafe(this.boundBean, "addPropertyChangeListener", new Object[]{this});
               } catch (MissingMethodException var3) {
                  throw new RuntimeException("Properties in beans of type " + PropertyBinding.this.bean.getClass().getName() + " are not observable in any capacity (no PropertyChangeListener support).");
               }
            }
         }

      }

      public void unbind() {
         if (this.bound) {
            if (this.boundToProperty) {
               try {
                  InvokerHelper.invokeMethodSafe(this.boundBean, "removePropertyChangeListener", new Object[]{this.boundProperty, this});
               } catch (MissingMethodException var3) {
               }
            } else {
               try {
                  InvokerHelper.invokeMethodSafe(this.boundBean, "removePropertyChangeListener", new Object[]{this});
               } catch (MissingMethodException var2) {
               }
            }

            this.boundBean = null;
            this.boundProperty = null;
            this.bound = false;
         }

      }

      public void rebind() {
         if (this.bound) {
            this.unbind();
            this.bind();
         }

      }
   }
}
