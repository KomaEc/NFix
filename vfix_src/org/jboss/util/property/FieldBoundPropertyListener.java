package org.jboss.util.property;

import java.beans.PropertyEditor;
import org.jboss.util.Classes;
import org.jboss.util.FieldInstance;
import org.jboss.util.NullArgumentException;
import org.jboss.util.ThrowableHandler;
import org.jboss.util.propertyeditor.PropertyEditors;

public class FieldBoundPropertyListener extends BoundPropertyAdapter {
   protected final String propertyName;
   protected final FieldInstance fieldInstance;

   public FieldBoundPropertyListener(Object instance, String fieldName, String propertyName) {
      if (propertyName == null) {
         throw new NullArgumentException("propertyName");
      } else {
         this.propertyName = propertyName;

         try {
            this.fieldInstance = new FieldInstance(instance, fieldName);

            try {
               this.fieldInstance.getField().setAccessible(true);
            } catch (SecurityException var5) {
               ThrowableHandler.add(var5);
            }

            Classes.forceLoad(this.fieldInstance.getField().getType());
         } catch (NoSuchFieldException var6) {
            throw new PropertyException(var6);
         }
      }
   }

   public FieldBoundPropertyListener(Object instance, String fieldName) {
      this(instance, fieldName, fieldName);
   }

   public final String getPropertyName() {
      return this.propertyName;
   }

   public String filterValue(String value) {
      return value;
   }

   protected void setFieldValue(String value) {
      try {
         value = this.filterValue(value);
         Class<?> type = this.fieldInstance.getField().getType();
         PropertyEditor editor = PropertyEditors.findEditor(type);
         editor.setAsText(value);
         Object coerced = editor.getValue();
         this.fieldInstance.set(coerced);
      } catch (IllegalAccessException var5) {
         throw new PropertyException(var5);
      }
   }

   public void propertyAdded(PropertyEvent event) {
      this.setFieldValue(event.getPropertyValue());
   }

   public void propertyChanged(PropertyEvent event) {
      this.setFieldValue(event.getPropertyValue());
   }

   public void propertyBound(PropertyMap map) {
      if (map.containsProperty(this.propertyName)) {
         this.setFieldValue(map.getProperty(this.propertyName));
      }

   }
}
