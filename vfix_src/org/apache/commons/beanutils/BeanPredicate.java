package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanPredicate implements Predicate {
   private final Log log = LogFactory.getLog(this.getClass());
   private String propertyName;
   private Predicate predicate;

   public BeanPredicate(String propertyName, Predicate predicate) {
      this.propertyName = propertyName;
      this.predicate = predicate;
   }

   public boolean evaluate(Object object) {
      boolean evaluation = false;

      try {
         Object propValue = PropertyUtils.getProperty(object, this.propertyName);
         evaluation = this.predicate.evaluate(propValue);
         return evaluation;
      } catch (IllegalArgumentException var8) {
         String errorMsg = "Problem during evaluation.";
         this.log.error("ERROR: Problem during evaluation.", var8);
         throw var8;
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
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   public Predicate getPredicate() {
      return this.predicate;
   }

   public void setPredicate(Predicate predicate) {
      this.predicate = predicate;
   }
}
