package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.util.Iterator;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$386 extends GeneratedMetaMethod {
   public dgm$386(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.max((Iterator)var1, (Closure)var2[0]);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      return DefaultGroovyMethods.max((Iterator)var1, (Closure)var2[0]);
   }
}
