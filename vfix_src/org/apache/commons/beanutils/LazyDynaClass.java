package org.apache.commons.beanutils;

public class LazyDynaClass extends BasicDynaClass implements MutableDynaClass {
   protected boolean restricted;
   protected boolean returnNull;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$LazyDynaBean;

   public LazyDynaClass() {
      this((String)null, (DynaProperty[])((DynaProperty[])null));
   }

   public LazyDynaClass(String name) {
      this(name, (DynaProperty[])null);
   }

   public LazyDynaClass(String name, Class dynaBeanClass) {
      this(name, dynaBeanClass, (DynaProperty[])null);
   }

   public LazyDynaClass(String name, DynaProperty[] properties) {
      this(name, class$org$apache$commons$beanutils$LazyDynaBean == null ? (class$org$apache$commons$beanutils$LazyDynaBean = class$("org.apache.commons.beanutils.LazyDynaBean")) : class$org$apache$commons$beanutils$LazyDynaBean, properties);
   }

   public LazyDynaClass(String name, Class dynaBeanClass, DynaProperty[] properties) {
      super(name, dynaBeanClass, properties);
      this.returnNull = false;
   }

   public boolean isRestricted() {
      return this.restricted;
   }

   public void setRestricted(boolean restricted) {
      this.restricted = restricted;
   }

   public boolean isReturnNull() {
      return this.returnNull;
   }

   public void setReturnNull(boolean returnNull) {
      this.returnNull = returnNull;
   }

   public void add(String name) {
      this.add(new DynaProperty(name));
   }

   public void add(String name, Class type) {
      this.add(new DynaProperty(name, type));
   }

   public void add(String name, Class type, boolean readable, boolean writeable) {
      throw new UnsupportedOperationException("readable/writable properties not supported");
   }

   protected void add(DynaProperty property) {
      if (property.getName() == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else if (this.isRestricted()) {
         throw new IllegalStateException("DynaClass is currently restricted. No new properties can be added.");
      } else if (this.propertiesMap.get(property.getName()) == null) {
         DynaProperty[] oldProperties = this.getDynaProperties();
         DynaProperty[] newProperties = new DynaProperty[oldProperties.length + 1];
         System.arraycopy(oldProperties, 0, newProperties, 0, oldProperties.length);
         newProperties[oldProperties.length] = property;
         this.setProperties(newProperties);
      }
   }

   public void remove(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else if (this.isRestricted()) {
         throw new IllegalStateException("DynaClass is currently restricted. No properties can be removed.");
      } else if (this.propertiesMap.get(name) != null) {
         DynaProperty[] oldProperties = this.getDynaProperties();
         DynaProperty[] newProperties = new DynaProperty[oldProperties.length - 1];
         int j = 0;

         for(int i = 0; i < oldProperties.length; ++i) {
            if (!name.equals(oldProperties[i].getName())) {
               newProperties[j] = oldProperties[i];
               ++j;
            }
         }

         this.setProperties(newProperties);
      }
   }

   public DynaProperty getDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else {
         DynaProperty dynaProperty = (DynaProperty)this.propertiesMap.get(name);
         if (dynaProperty == null && !this.isReturnNull() && !this.isRestricted()) {
            dynaProperty = new DynaProperty(name);
         }

         return dynaProperty;
      }
   }

   public boolean isDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else {
         return this.propertiesMap.get(name) != null;
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
