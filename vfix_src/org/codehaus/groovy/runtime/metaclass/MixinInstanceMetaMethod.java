package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.MixinInMetaClass;

public class MixinInstanceMetaMethod extends MetaMethod {
   private final MetaMethod method;
   private final MixinInMetaClass mixinInMetaClass;

   public MixinInstanceMetaMethod(MetaMethod method, MixinInMetaClass mixinInMetaClass) {
      this.method = method;
      this.mixinInMetaClass = mixinInMetaClass;
   }

   public int getModifiers() {
      return this.method.getModifiers();
   }

   public String getName() {
      return this.method.getName();
   }

   public Class getReturnType() {
      return this.method.getReturnType();
   }

   public CachedClass getDeclaringClass() {
      return this.mixinInMetaClass.getInstanceClass();
   }

   public Object invoke(Object object, Object[] arguments) {
      this.method.getParameterTypes();
      return this.method.invoke(this.mixinInMetaClass.getMixinInstance(object), this.method.correctArguments(arguments));
   }

   protected Class[] getPT() {
      return this.method.getNativeParameterTypes();
   }
}
