package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.io.InputStream;
import java.io.Writer;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$183 extends GeneratedMetaMethod {
   public dgm$183(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DefaultGroovyMethods.filterLine((InputStream)var1, (Writer)var2[0], (String)var2[1], (Closure)var2[2]);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DefaultGroovyMethods.filterLine((InputStream)var1, (Writer)var2[0], (String)var2[1], (Closure)var2[2]);
      return null;
   }
}
