package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.math.BigDecimal;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$711 extends GeneratedMetaMethod {
   public dgm$711(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DefaultGroovyMethods.upto((BigDecimal)var1, (Number)var2[0], (Closure)var2[1]);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DefaultGroovyMethods.upto((BigDecimal)var1, (Number)var2[0], (Closure)var2[1]);
      return null;
   }
}
