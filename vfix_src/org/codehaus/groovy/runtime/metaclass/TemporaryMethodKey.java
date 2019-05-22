package org.codehaus.groovy.runtime.metaclass;

import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.MethodKey;

public class TemporaryMethodKey extends MethodKey {
   private final Object[] parameterValues;

   public TemporaryMethodKey(Class sender, String name, Object[] parameterValues, boolean isCallToSuper) {
      super(sender, name, isCallToSuper);
      if (parameterValues == null) {
         parameterValues = MetaClassHelper.EMPTY_ARRAY;
      }

      this.parameterValues = parameterValues;
   }

   public int getParameterCount() {
      return this.parameterValues.length;
   }

   public Class getParameterType(int index) {
      Object value = this.parameterValues[index];
      if (value != null) {
         Class type = (Class)((Class)(value.getClass() == Class.class ? value : value.getClass()));
         return type;
      } else {
         return Object.class;
      }
   }
}
