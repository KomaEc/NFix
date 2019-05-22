package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanToPropertyValueTransformer implements Transformer {
   private final Log log;
   private String propertyName;
   private boolean ignoreNull;

   public BeanToPropertyValueTransformer(String propertyName) {
      this(propertyName, false);
   }

   public BeanToPropertyValueTransformer(String propertyName, boolean ignoreNull) {
      this.log = LogFactory.getLog(this.getClass());
      if (propertyName != null && propertyName.length() > 0) {
         this.propertyName = propertyName;
         this.ignoreNull = ignoreNull;
      } else {
         throw new IllegalArgumentException("propertyName cannot be null or empty");
      }
   }

   public Object transform(Object object) {
      Object propertyValue = null;

      try {
         propertyValue = PropertyUtils.getProperty(object, this.propertyName);
      } catch (IllegalArgumentException var8) {
         String errorMsg = "Problem during transformation. Null value encountered in property path...";
         if (!this.ignoreNull) {
            this.log.error("ERROR: Problem during transformation. Null value encountered in property path...", var8);
            throw var8;
         }

         this.log.warn("WARNING: Problem during transformation. Null value encountered in property path...", var8);
      } catch (IllegalAccessException var9) {
         String errorMsg = "Unable to access the property provided.";
         this.log.error("Unable to access the property provided.", var9);
         throw new IllegalArgumentException("Unable to access the property provided.");
      } catch (InvocationTargetException var10) {
         String errorMsg = "Exception occurred in property's getter";
         this.log.error("Exception occurred in property's getter", var10);
         throw new IllegalArgumentException("Exception occurred in property's getter");
      } catch (NoSuchMethodException var11) {
         String errorMsg = "No property found for name [" + this.propertyName + "]";
         this.log.error(errorMsg, var11);
         throw new IllegalArgumentException(errorMsg);
      }

      return propertyValue;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public boolean isIgnoreNull() {
      return this.ignoreNull;
   }
}
