package org.codehaus.groovy.runtime;

import java.util.Date;
import java.util.Map;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$861 extends GeneratedMetaMethod {
   public dgm$861(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DateGroovyMethods.set((Date)var1, (Map)var2[0]);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DateGroovyMethods.set((Date)var1, (Map)var2[0]);
      return null;
   }
}
