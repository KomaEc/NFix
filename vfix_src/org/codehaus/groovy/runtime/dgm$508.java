package org.codehaus.groovy.runtime;

import groovy.lang.EmptyRange;
import java.util.Collection;
import java.util.List;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$508 extends GeneratedMetaMethod {
   public dgm$508(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DefaultGroovyMethods.putAt((List)var1, (EmptyRange)var2[0], (Collection)var2[1]);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DefaultGroovyMethods.putAt((List)var1, (EmptyRange)var2[0], (Collection)var2[1]);
      return null;
   }
}
