package org.codehaus.groovy.runtime.metaclass;

import org.codehaus.groovy.reflection.CachedMethod;

public class NewStaticMetaMethod extends NewMetaMethod {
   public NewStaticMetaMethod(CachedMethod method) {
      super(method);
   }

   public boolean isStatic() {
      return true;
   }

   public int getModifiers() {
      return 9;
   }

   public Object invoke(Object object, Object[] arguments) {
      int size = arguments.length;
      Object[] newArguments = new Object[size + 1];
      System.arraycopy(arguments, 0, newArguments, 1, size);
      newArguments[0] = null;
      return super.invoke((Object)null, newArguments);
   }
}
