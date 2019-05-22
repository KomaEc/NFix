package org.codehaus.groovy.runtime;

import org.codehaus.groovy.reflection.CachedClass;

public class DefaultCachedMethodKey extends MethodKey {
   private final CachedClass[] parameterTypes;

   public DefaultCachedMethodKey(Class sender, String name, CachedClass[] parameterTypes, boolean isCallToSuper) {
      super(sender, name, isCallToSuper);
      this.parameterTypes = parameterTypes;
   }

   public int getParameterCount() {
      return this.parameterTypes.length;
   }

   public Class getParameterType(int index) {
      CachedClass c = this.parameterTypes[index];
      return c == null ? Object.class : c.getTheClass();
   }
}
