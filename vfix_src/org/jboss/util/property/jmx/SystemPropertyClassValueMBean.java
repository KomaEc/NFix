package org.jboss.util.property.jmx;

public interface SystemPropertyClassValueMBean {
   String getProperty();

   void setProperty(String var1);

   String getClassName();

   void setClassName(String var1);

   void create();
}
