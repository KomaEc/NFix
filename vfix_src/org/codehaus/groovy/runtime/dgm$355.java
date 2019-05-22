package org.codehaus.groovy.runtime;

import java.util.regex.Matcher;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$355 extends GeneratedMetaMethod {
   public dgm$355(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return DefaultGroovyMethods.iterator((Matcher)var1);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      this.coerceArgumentsToClasses(var2);
      return DefaultGroovyMethods.iterator((Matcher)var1);
   }
}
