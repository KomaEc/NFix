package org.codehaus.groovy.runtime;

import java.awt.Component;
import javax.swing.JPopupMenu;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;

public class dgm$802 extends GeneratedMetaMethod {
   public dgm$802(String var1, CachedClass var2, Class var3, Class[] var4) {
      super(var1, var2, var3, var4);
   }

   public Object invoke(Object var1, Object[] var2) {
      return SwingGroovyMethods.leftShift((JPopupMenu)var1, (Component)var2[0]);
   }

   public final Object doMethodInvoke(Object var1, Object[] var2) {
      var2 = this.coerceArgumentsToClasses(var2);
      return SwingGroovyMethods.leftShift((JPopupMenu)var1, (Component)var2[0]);
   }
}
