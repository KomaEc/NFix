package org.codehaus.groovy.runtime;

import groovy.lang.IntRange;
import java.util.BitSet;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$505 extends GeneratedMetaMethod {
   public dgm$505(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DefaultGroovyMethods.putAt((BitSet)var1, (IntRange)var2[0], DefaultTypeTransformation.booleanUnbox(var2[1]));
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DefaultGroovyMethods.putAt((BitSet)var1, (IntRange)var2[0], DefaultTypeTransformation.booleanUnbox(var2[1]));
      return null;
   }
}
