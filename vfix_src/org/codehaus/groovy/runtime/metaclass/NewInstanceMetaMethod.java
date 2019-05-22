package org.codehaus.groovy.runtime.metaclass;

import org.codehaus.groovy.reflection.CachedMethod;

public class NewInstanceMetaMethod extends NewMetaMethod {
   public NewInstanceMetaMethod(CachedMethod method) {
      super(method);
   }

   public boolean isStatic() {
      return false;
   }

   public int getModifiers() {
      return 1;
   }

   public Object invoke(Object object, Object[] arguments) {
      int size = arguments.length;
      Object[] newArguments = new Object[size + 1];
      newArguments[0] = object;
      System.arraycopy(arguments, 0, newArguments, 1, size);
      return super.invoke((Object)null, newArguments);
   }
}
