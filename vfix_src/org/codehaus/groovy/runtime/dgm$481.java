package org.codehaus.groovy.runtime;

import java.math.BigInteger;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$481 extends GeneratedMetaMethod {
   public dgm$481(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.power((BigInteger)var1, (Integer)var2[0]);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.power((BigInteger)var1, (Integer)this.getParameterTypes()[0].coerceArgument(var2[0]));
   }

   public boolean isValidMethod(Class[] var1) {
      return var1 == null || this.getParameterTypes()[0].isAssignableFrom(var1[0]);
   }
}
