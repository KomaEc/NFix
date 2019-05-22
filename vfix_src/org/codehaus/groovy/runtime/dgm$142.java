package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.io.File;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$142 extends GeneratedMetaMethod {
   public dgm$142(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.eachLine((File)var1, (String)var2[0], DefaultTypeTransformation.intUnbox(var2[1]), (Closure)var2[2]);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      return DefaultGroovyMethods.eachLine((File)var1, (String)var2[0], DefaultTypeTransformation.intUnbox(var2[1]), (Closure)var2[2]);
   }
}
