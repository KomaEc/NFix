package org.codehaus.groovy.runtime;

import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$22 extends GeneratedMetaMethod {
   public dgm$22(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultTypeTransformation.box(DefaultGroovyMethods.asBoolean((float[])var1));
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      this.coerceArgumentsToClasses(var2);
      return DefaultTypeTransformation.box(DefaultGroovyMethods.asBoolean((float[])var1));
   }
}
