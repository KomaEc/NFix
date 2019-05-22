package org.apache.commons.beanutils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;

public class WrapDynaClass implements DynaClass {
   protected Class beanClass = null;
   protected PropertyDescriptor[] descriptors = null;
   protected HashMap descriptorsMap = new HashMap();
   protected DynaProperty[] properties = null;
   protected HashMap propertiesMap = new HashMap();
   protected static HashMap dynaClasses = new HashMap();
   // $FF: synthetic field
   static Class class$java$util$Map;

   private WrapDynaClass(Class beanClass) {
      this.beanClass = beanClass;
      this.introspect();
   }

   public String getName() {
      return this.beanClass.getName();
   }

   public DynaProperty getDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         return (DynaProperty)this.propertiesMap.get(name);
      }
   }

   public DynaProperty[] getDynaProperties() {
      return this.properties;
   }

   public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
      return new WrapDynaBean(this.beanClass.newInstance());
   }

   public PropertyDescriptor getPropertyDescriptor(String name) {
      return (PropertyDescriptor)this.descriptorsMap.get(name);
   }

   public static void clear() {
      synchronized(dynaClasses) {
         dynaClasses.clear();
      }
   }

   public static WrapDynaClass createDynaClass(Class beanClass) {
      synchronized(dynaClasses) {
         WrapDynaClass dynaClass = (WrapDynaClass)dynaClasses.get(beanClass);
         if (dynaClass == null) {
            dynaClass = new WrapDynaClass(beanClass);
            dynaClasses.put(beanClass, dynaClass);
         }

         return dynaClass;
      }
   }

   protected void introspect() {
      PropertyDescriptor[] regulars = PropertyUtils.getPropertyDescriptors(this.beanClass);
      if (regulars == null) {
         regulars = new PropertyDescriptor[0];
      }

      HashMap mappeds = PropertyUtils.getMappedPropertyDescriptors(this.beanClass);
      if (mappeds == null) {
         mappeds = new HashMap();
      }

      this.properties = new DynaProperty[regulars.length + ((HashMap)mappeds).size()];

      for(int i = 0; i < regulars.length; ++i) {
         this.descriptorsMap.put(regulars[i].getName(), regulars[i]);
         this.properties[i] = new DynaProperty(regulars[i].getName(), regulars[i].getPropertyType());
         this.propertiesMap.put(this.properties[i].getName(), this.properties[i]);
      }

      int j = regulars.length;

      for(Iterator names = ((HashMap)mappeds).keySet().iterator(); names.hasNext(); ++j) {
         String name = (String)names.next();
         PropertyDescriptor descriptor = (PropertyDescriptor)((HashMap)mappeds).get(name);
         this.properties[j] = new DynaProperty(descriptor.getName(), class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
         this.propertiesMap.put(this.properties[j].getName(), this.properties[j]);
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
