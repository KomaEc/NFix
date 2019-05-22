package org.codehaus.groovy.runtime;

import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$79 extends GeneratedMetaMethod {
   public dgm$79(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultTypeTransformation.box(DefaultGroovyMethods.compareTo((Number)var1, (Number)var2[0]));
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      return DefaultTypeTransformation.box(DefaultGroovyMethods.compareTo((Number)var1, (Number)this.getParameterTypes()[0].coerceArgument(var2[0])));
   }

   public boolean isValidMethod(Class[] var1) {
      return var1 == null || this.getParameterTypes()[0].isAssignableFrom(var1[0]);
   }
}
