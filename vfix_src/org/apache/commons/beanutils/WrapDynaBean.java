package org.apache.commons.beanutils;

public class WrapDynaBean implements DynaBean {
   protected WrapDynaClass dynaClass = null;
   protected Object instance = null;

   public WrapDynaBean(Object instance) {
      this.instance = instance;
      this.dynaClass = WrapDynaClass.createDynaClass(instance.getClass());
   }

   public boolean contains(String name, String key) {
      throw new UnsupportedOperationException("WrapDynaBean does not support contains()");
   }

   public Object get(String name) {
      Object value = null;

      try {
         value = PropertyUtils.getSimpleProperty(this.instance, name);
         return value;
      } catch (Throwable var4) {
         throw new IllegalArgumentException("Property '" + name + "' has no read method");
      }
   }

   public Object get(String name, int index) {
      Object value = null;

      try {
         value = PropertyUtils.getIndexedProperty(this.instance, name, index);
         return value;
      } catch (IndexOutOfBoundsException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new IllegalArgumentException("Property '" + name + "' has no indexed read method");
      }
   }

   public Object get(String name, String key) {
      Object value = null;

      try {
         value = PropertyUtils.getMappedProperty(this.instance, name, key);
         return value;
      } catch (Throwable var5) {
         throw new IllegalArgumentException("Property '" + name + "' has no mapped read method");
      }
   }

   public DynaClass getDynaClass() {
      return this.dynaClass;
   }

   public void remove(String name, String key) {
      throw new UnsupportedOperationException("WrapDynaBean does not support remove()");
   }

   public void set(String name, Object value) {
      try {
         PropertyUtils.setSimpleProperty(this.instance, name, value);
      } catch (Throwable var4) {
         throw new IllegalArgumentException("Property '" + name + "' has no write method");
      }
   }

   public void set(String name, int index, Object value) {
      try {
         PropertyUtils.setIndexedProperty(this.instance, name, index, value);
      } catch (IndexOutOfBoundsException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new IllegalArgumentException("Property '" + name + "' has no indexed write method");
      }
   }

   public void set(String name, String key, Object value) {
      try {
         PropertyUtils.setMappedProperty(this.instance, name, key, value);
      } catch (Throwable var5) {
         throw new IllegalArgumentException("Property '" + name + "' has no mapped write method");
      }
   }

   public Object getInstance() {
      return this.instance;
   }

   protected DynaProperty getDynaProperty(String name) {
      DynaProperty descriptor = this.getDynaClass().getDynaProperty(name);
      if (descriptor == null) {
         throw new IllegalArgumentException("Invalid property name '" + name + "'");
      } else {
         return descriptor;
      }
   }
}
