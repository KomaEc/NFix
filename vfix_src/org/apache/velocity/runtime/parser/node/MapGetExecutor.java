package org.apache.velocity.runtime.parser.node;

import java.util.Map;
import org.apache.velocity.runtime.log.Log;

public class MapGetExecutor extends AbstractExecutor {
   private final String property;
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public MapGetExecutor(Log log, Class clazz, String property) {
      this.log = log;
      this.property = property;
      this.discover(clazz);
   }

   protected void discover(Class clazz) {
      Class[] interfaces = clazz.getInterfaces();

      for(int i = 0; i < interfaces.length; ++i) {
         if (interfaces[i].equals(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map)) {
            try {
               if (this.property != null) {
                  this.setMethod((class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).getMethod("get", class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object));
               }
               break;
            } catch (RuntimeException var5) {
               throw var5;
            } catch (Exception var6) {
               this.log.error("While looking for get('" + this.property + "') method:", var6);
               break;
            }
         }
      }

   }

   public Object execute(Object o) {
      return ((Map)o).get(this.property);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
