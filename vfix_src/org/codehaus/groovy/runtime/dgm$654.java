package org.codehaus.groovy.runtime;

import java.util.Enumeration;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$654 extends GeneratedMetaMethod {
   public dgm$654(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.toList((Enumeration)var1);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      this.coerceArgumentsToClasses(var2);
      return DefaultGroovyMethods.toList((Enumeration)var1);
   }
}
