package org.apache.commons.beanutils;

import java.util.Iterator;
import java.util.Map;

public class LazyDynaMap extends LazyDynaBean implements MutableDynaClass {
   protected String name;
   protected boolean restricted;
   protected boolean returnNull;

   public LazyDynaMap() {
      this((String)null, (Map)((Map)null));
   }

   public LazyDynaMap(String name) {
      this(name, (Map)null);
   }

   public LazyDynaMap(Map values) {
      this((String)null, (Map)values);
   }

   public LazyDynaMap(String name, Map values) {
      this.returnNull = false;
      this.name = name == null ? "LazyDynaMap" : name;
      this.values = values == null ? this.newMap() : values;
      this.dynaClass = this;
   }

   public LazyDynaMap(DynaProperty[] properties) {
      this((String)null, (DynaProperty[])properties);
   }

   public LazyDynaMap(String name, DynaProperty[] properties) {
      this(name, (Map)null);
      if (properties != null) {
         for(int i = 0; i < properties.length; ++i) {
            this.add(properties[i]);
         }
      }

   }

   public LazyDynaMap(DynaClass dynaClass) {
      this(dynaClass.getName(), dynaClass.getDynaProperties());
   }

   public void setMap(Map values) {
      this.values = values;
   }

   public void set(String name, Object value) {
      if (this.isRestricted() && !this.values.containsKey(name)) {
         throw new IllegalArgumentException("Invalid property name '" + name + "' (DynaClass is restricted)");
      } else {
         this.values.put(name, value);
      }
   }

   public String getName() {
      return this.name;
   }

   public DynaProperty getDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else if (!this.values.containsKey(name) && this.isReturnNull()) {
         return null;
      } else {
         Object value = this.values.get(name);
         return value == null ? new DynaProperty(name) : new DynaProperty(name, value.getClass());
      }
   }

   public DynaProperty[] getDynaProperties() {
      int i = 0;
      DynaProperty[] properties = new DynaProperty[this.values.size()];

      String name;
      Object value;
      for(Iterator iterator = this.values.keySet().iterator(); iterator.hasNext(); properties[i++] = new DynaProperty(name, value == null ? null : value.getClass())) {
         name = (String)iterator.next();
         value = this.values.get(name);
      }

      return properties;
   }

   public DynaBean newInstance() {
      return new LazyDynaMap(this);
   }

   public boolean isRestricted() {
      return this.restricted;
   }

   public void setRestricted(boolean restricted) {
      this.restricted = restricted;
   }

   public void add(String name) {
      this.add(name, (Class)null);
   }

   public void add(String name, Class type) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else if (this.isRestricted()) {
         throw new IllegalStateException("DynaClass is currently restricted. No new properties can be added.");
      } else {
         Object value = this.values.get(name);
         if (value == null) {
            this.values.put(name, type == null ? null : this.createProperty(name, type));
         }

      }
   }

   public void add(String name, Class type, boolean readable, boolean writeable) {
      throw new UnsupportedOperationException("readable/writable properties not supported");
   }

   protected void add(DynaProperty property) {
      this.add(property.getName(), property.getType());
   }

   public void remove(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else if (this.isRestricted()) {
         throw new IllegalStateException("DynaClass is currently restricted. No properties can be removed.");
      } else {
         if (this.values.containsKey(name)) {
            this.values.remove(name);
         }

      }
   }

   public boolean isReturnNull() {
      return this.returnNull;
   }

   public void setReturnNull(boolean returnNull) {
      this.returnNull = returnNull;
   }

   protected boolean isDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Property name is missing.");
      } else {
         return this.values.containsKey(name);
      }
   }
}
