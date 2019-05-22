package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanPropertyValueEqualsPredicate implements Predicate {
   private final Log log;
   private String propertyName;
   private Object propertyValue;
   private boolean ignoreNull;

   public BeanPropertyValueEqualsPredicate(String propertyName, Object propertyValue) {
      this(propertyName, propertyValue, false);
   }

   public BeanPropertyValueEqualsPredicate(String propertyName, Object propertyValue, boolean ignoreNull) {
      this.log = LogFactory.getLog(this.getClass());
      if (propertyName != null && propertyName.length() > 0) {
         this.propertyName = propertyName;
         this.propertyValue = propertyValue;
         this.ignoreNull = ignoreNull;
      } else {
         throw new IllegalArgumentException("propertyName cannot be null or empty");
      }
   }

   public boolean evaluate(Object object) {
      boolean evaluation = false;

      try {
         evaluation = this.evaluateValue(this.propertyValue, PropertyUtils.getProperty(object, this.propertyName));
      } catch (IllegalArgumentException var8) {
         String errorMsg = "Problem during evaluation. Null value encountered in property path...";
         if (!this.ignoreNull) {
            this.log.error("ERROR: Problem during evaluation. Null value encountered in property path...", var8);
            throw var8;
         }

         this.log.warn("WARNING: Problem during evaluation. Null value encountered in property path...", var8);
      } catch (IllegalAccessException var9) {
         String errorMsg = "Unable to access the property provided.";
         this.log.error("Unable to access the property provided.", var9);
         throw new IllegalArgumentException("Unable to access the property provided.");
      } catch (InvocationTargetException var10) {
         String errorMsg = "Exception occurred in property's getter";
         this.log.error("Exception occurred in property's getter", var10);
         throw new IllegalArgumentException("Exception occurred in property's getter");
      } catch (NoSuchMethodException var11) {
         String errorMsg = "Property not found.";
         this.log.error("Property not found.", var11);
         throw new IllegalArgumentException("Property not found.");
      }

      return evaluation;
   }

   private boolean evaluateValue(Object expected, Object actual) {
      return expected == actual || expected != null && expected.equals(actual);
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
