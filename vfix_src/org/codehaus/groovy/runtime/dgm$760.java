package org.codehaus.groovy.runtime;

import javax.swing.DefaultListModel;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$760 extends GeneratedMetaMethod {
   public dgm$760(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      SwingGroovyMethods.clear((DefaultListModel)var1);
      return null;
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      this.coerceArgumentsToClasses(var2);
      SwingGroovyMethods.clear((DefaultListModel)var1);
      return null;
   }
}
