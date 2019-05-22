package org.apache.commons.beanutils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class BasicDynaClass implements DynaClass, Serializable {
   protected transient Constructor constructor;
   protected static Class[] constructorTypes;
   protected Object[] constructorValues;
   protected Class dynaBeanClass;
   protected String name;
   protected DynaProperty[] properties;
   protected HashMap propertiesMap;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$BasicDynaBean;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$DynaClass;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$DynaBean;

   public BasicDynaClass() {
      this((String)null, (Class)null, (DynaProperty[])null);
   }

   public BasicDynaClass(String name, Class dynaBeanClass) {
      this(name, dynaBeanClass, (DynaProperty[])null);
   }

   public BasicDynaClass(String name, Class dynaBeanClass, DynaProperty[] properties) {
      this.constructor = null;
      this.constructorValues = new Object[]{this};
      this.dynaBeanClass = class$org$apache$commons$beanutils$BasicDynaBean == null ? (class$org$apache$commons$beanutils$BasicDynaBean = class$("org.apache.commons.beanutils.BasicDynaBean")) : class$org$apache$commons$beanutils$BasicDynaBean;
      this.name = this.getClass().getName();
      this.properties = new DynaProperty[0];
      this.propertiesMap = new HashMap();
      if (name != null) {
         this.name = name;
      }

      if (dynaBeanClass == null) {
         dynaBeanClass = class$org$apache$commons$beanutils$BasicDynaBean == null ? (class$org$apache$commons$beanutils$BasicDynaBean = class$("org.apache.commons.beanutils.BasicDynaBean")) : class$org$apache$commons$beanutils$BasicDynaBean;
      }

      this.setDynaBeanClass(dynaBeanClass);
      if (properties != null) {
         this.setProperties(properties);
      }

   }

   public String getName() {
      return this.name;
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
      try {
         if (this.constructor == null) {
            this.setDynaBeanClass(this.dynaBeanClass);
         }

         return (DynaBean)this.constructor.newInstance(this.constructorValues);
      } catch (InvocationTargetException var2) {
         throw new InstantiationException(var2.getTargetException().getMessage());
      }
   }

   public Class getDynaBeanClass() {
      return this.dynaBeanClass;
   }

   protected void setDynaBeanClass(Class dynaBeanClass) {
      if (dynaBeanClass.isInterface()) {
         throw new IllegalArgumentException("Class " + dynaBeanClass.getName() + " is an interface, not a class");
      } else if (!(class$org$apache$commons$beanutils$DynaBean == null ? (class$org$apache$commons$beanutils$DynaBean = class$("org.apache.commons.beanutils.DynaBean")) : class$org$apache$commons$beanutils$DynaBean).isAssignableFrom(dynaBeanClass)) {
         throw new IllegalArgumentException("Class " + dynaBeanClass.getName() + " does not implement DynaBean");
      } else {
         try {
            this.constructor = dynaBeanClass.getConstructor(constructorTypes);
         } catch (NoSuchMethodException var3) {
            throw new IllegalArgumentException("Class " + dynaBeanClass.getName() + " does not have an appropriate constructor");
         }

         this.dynaBeanClass = dynaBeanClass;
      }
   }

   protected void setProperties(DynaProperty[] properties) {
      this.properties = properties;
      this.propertiesMap.clear();

      for(int i = 0; i < properties.length; ++i) {
         this.propertiesMap.put(properties[i].getName(), properties[i]);
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

   static {
      constructorTypes = new Class[]{class$org$apache$commons$beanutils$DynaClass == null ? (class$org$apache$commons$beanutils$DynaClass = class$("org.apache.commons.beanutils.DynaClass")) : class$org$apache$commons$beanutils$DynaClass};
   }
}
