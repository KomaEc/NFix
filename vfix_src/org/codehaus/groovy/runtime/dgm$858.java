package org.codehaus.groovy.runtime;

import java.util.Calendar;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class dgm$858 extends GeneratedMetaMethod {
   public dgm$858(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      DateGroovyMethods.putAt((Calendar)var1, DefaultTypeTransformation.intUnbox(var2[0]), DefaultTypeTransformation.intUnbox(var2[1]));
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      DateGroovyMethods.putAt((Calendar)var1, DefaultTypeTransformation.intUnbox(var2[0]), DefaultTypeTransformation.intUnbox(var2[1]));
      return null;
   }
}
