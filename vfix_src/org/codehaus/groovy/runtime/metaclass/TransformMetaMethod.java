package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;

public class TransformMetaMethod extends MetaMethod {
   private MetaMethod metaMethod;

   public TransformMetaMethod(MetaMethod metaMethod) {
      this.metaMethod = metaMethod;
      this.setParametersTypes(metaMethod.getParameterTypes());
      this.nativeParamTypes = metaMethod.getNativeParameterTypes();
   }

   public int getModifiers() {
      return this.metaMethod.getModifiers();
   }

   public String getName() {
      return this.metaMethod.getName();
   }

   public Class getReturnType() {
      return this.metaMethod.getReturnType();
   }

   public CachedClass getDeclaringClass() {
      return this.metaMethod.getDeclaringClass();
   }

   public Object invoke(Object object, Object[] arguments) {
      return this.metaMethod.invoke(object, arguments);
   }
}
