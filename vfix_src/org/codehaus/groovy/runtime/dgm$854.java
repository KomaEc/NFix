package org.codehaus.groovy.runtime;

import java.sql.Date;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$854 extends GeneratedMetaMethod {
   public dgm$854(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DateGroovyMethods.plus((Date)var1, DefaultTypeTransformation.intUnbox(var2[0]));
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      return DateGroovyMethods.plus((Date)var1, DefaultTypeTransformation.intUnbox(var2[0]));
   }
}
