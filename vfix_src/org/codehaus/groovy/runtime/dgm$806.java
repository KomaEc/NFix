package org.codehaus.groovy.runtime;

import javax.swing.Action;
import javax.swing.JToolBar;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$806 extends GeneratedMetaMethod {
   public dgm$806(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return SwingGroovyMethods.leftShift((JToolBar)var1, (Action)var2[0]);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      return SwingGroovyMethods.leftShift((JToolBar)var1, (Action)var2[0]);
   }
}
