package org.codehaus.groovy.runtime;

public class DefaultMethodKey extends MethodKey {
   private final Class[] parameterTypes;

   public DefaultMethodKey(Class sender, String name, Class[] parameterTypes, boolean isCallToSuper) {
      super(sender, name, isCallToSuper);
      this.parameterTypes = parameterTypes;
   }

   public int getParameterCount() {
      return this.parameterTypes.length;
   }

   public Class getParameterType(int index) {
      Class c = this.parameterTypes[index];
      return c == null ? Object.class : c;
   }
}
