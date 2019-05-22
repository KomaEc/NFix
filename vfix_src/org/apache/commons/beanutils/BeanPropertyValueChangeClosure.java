package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanPropertyValueChangeClosure implements Closure {
   private final Log log;
   private String propertyName;
   private Object propertyValue;
   private boolean ignoreNull;

   public BeanPropertyValueChangeClosure(String propertyName, Object propertyValue) {
      this(propertyName, propertyValue, false);
   }

   public BeanPropertyValueChangeClosure(String propertyName, Object propertyValue, boolean ignoreNull) {
      this.log = LogFactory.getLog(this.getClass());
      if (propertyName != null && propertyName.length() > 0) {
         this.propertyName = propertyName;
         this.propertyValue = propertyValue;
         this.ignoreNull = ignoreNull;
      } else {
         throw new IllegalArgumentException("propertyName cannot be null or empty");
      }
   }

   public void execute(Object object) {
      try {
         PropertyUtils.setProperty(object, this.propertyName, this.propertyValue);
      } catch (IllegalArgumentException var7) {
         String errorMsg = "Unable to execute Closure. Null value encountered in property path...";
         if (!this.ignoreNull) {
            this.log.error("ERROR: Unable to execute Closure. Null value encountered in property path...", var7);
            throw var7;
         }

         this.log.warn("WARNING: Unable to execute Closure. Null value encountered in property path...", var7);
      } catch (IllegalAccessException var8) {
         String errorMsg = "Unable to access the property provided.";
         this.log.error("Unable to access the property provided.", var8);
         throw new IllegalArgumentException("Unable to access the property provided.");
      } catch (InvocationTargetException var9) {
         String errorMsg = "Exception occurred in property's getter";
         this.log.error("Exception occurred in property's getter", var9);
         throw new IllegalArgumentException("Exception occurred in property's getter");
      } catch (NoSuchMethodException var10) {
         String errorMsg = "Property not found";
         this.log.error("Property not found", var10);
         throw new IllegalArgumentException("Property not found");
      }

   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public Object getPropertyValue() {
      return this.propertyValue;
   }

   public boolean isIgnoreNull() {
      return this.ignoreNull;
   }
}
