package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;

public class GetExecutor extends AbstractExecutor {
   private final Introspector introspector;
   private Object[] params;

   public GetExecutor(Log log, Introspector introspector, Class clazz, String property) {
      this.params = new Object[0];
      this.log = log;
      this.introspector = introspector;
      if (property != null) {
         this.params = new Object[]{property};
      }

      this.discover(clazz);
   }

   /** @deprecated */
   public GetExecutor(RuntimeLogger rlog, Introspector introspector, Class clazz, String property) {
      this((Log)(new RuntimeLoggerLog(rlog)), introspector, clazz, property);
   }

   protected void discover(Class clazz) {
      try {
         this.setMethod(this.introspector.getMethod(clazz, "get", this.params));
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         this.log.error("While looking for get('" + this.params[0] + "') method:", var4);
      }

   }

   public Object execute(Object o) throws IllegalAccessException, InvocationTargetException {
      return this.isAlive() ? this.getMethod().invoke(o, this.params) : null;
   }
}
