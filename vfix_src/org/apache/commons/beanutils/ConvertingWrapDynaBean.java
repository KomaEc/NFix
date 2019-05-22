package org.apache.commons.beanutils;

public class ConvertingWrapDynaBean extends WrapDynaBean {
   public ConvertingWrapDynaBean(Object instance) {
      super(instance);
   }

   public void set(String name, Object value) {
      try {
         BeanUtils.copyProperty(this.instance, name, value);
      } catch (Throwable var4) {
         throw new IllegalArgumentException("Property '" + name + "' has no write method");
      }
   }
}
