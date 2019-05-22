package org.codehaus.groovy.runtime;

import groovy.lang.Writable;
import java.io.Writer;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$752 extends GeneratedMetaMethod {
   public dgm$752(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DefaultGroovyMethods.write((Writer)var1, (Writable)var2[0]);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DefaultGroovyMethods.write((Writer)var1, (Writable)var2[0]);
      return null;
   }
}
