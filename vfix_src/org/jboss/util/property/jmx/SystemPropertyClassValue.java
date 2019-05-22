package org.jboss.util.property.jmx;

import org.jboss.logging.Logger;

public class SystemPropertyClassValue implements SystemPropertyClassValueMBean {
   public static final Logger log = Logger.getLogger(SystemPropertyClassValue.class);
   protected String property;
   protected String className;

   public String getProperty() {
      return this.property;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public void create() {
      Throwable error = setSystemPropertyClassValue(this.property, this.className);
      if (error != null) {
         log.trace("Error loading class " + this.className + " property " + this.property + " not set.", error);
      }

   }

   public static Throwable setSystemPropertyClassValue(String property, String className) {
      if (property != null && property.trim().length() != 0) {
         if (className != null && className.trim().length() != 0) {
            try {
               Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (Throwable var3) {
               return var3;
            }

            System.setProperty(property, className);
            return null;
         } else {
            throw new IllegalArgumentException("Null or empty class name");
         }
      } else {
         throw new IllegalArgumentException("Null or empty property");
      }
   }
}
