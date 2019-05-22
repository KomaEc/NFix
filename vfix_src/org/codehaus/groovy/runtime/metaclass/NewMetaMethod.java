package org.codehaus.groovy.runtime.metaclass;

import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;

public class NewMetaMethod extends ReflectionMetaMethod {
   protected static final CachedClass[] EMPTY_TYPE_ARRAY = new CachedClass[0];
   protected CachedClass[] bytecodeParameterTypes;

   public NewMetaMethod(CachedMethod method) {
      super(method);
      this.bytecodeParameterTypes = method.getParameterTypes();
      int size = this.bytecodeParameterTypes.length;
      CachedClass[] logicalParameterTypes;
      if (size <= 1) {
         logicalParameterTypes = EMPTY_TYPE_ARRAY;
      } else {
         --size;
         logicalParameterTypes = new CachedClass[size];
         System.arraycopy(this.bytecodeParameterTypes, 1, logicalParameterTypes, 0, size);
      }

      this.setParametersTypes(logicalParameterTypes);
   }

   public CachedClass getDeclaringClass() {
      return this.getBytecodeParameterTypes()[0];
   }

   public CachedClass[] getBytecodeParameterTypes() {
      return this.bytecodeParameterTypes;
   }

   public CachedClass getOwnerClass() {
      return this.getBytecodeParameterTypes()[0];
   }
}
