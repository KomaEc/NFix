package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.util.introspection.Introspector;

public class PutExecutor extends SetExecutor {
   private final Introspector introspector;
   private final String property;

   public PutExecutor(Log log, Introspector introspector, Class clazz, Object arg, String property) {
      this.log = log;
      this.introspector = introspector;
      this.property = property;
      this.discover(clazz, arg);
   }

   protected void discover(Class clazz, Object arg) {
      Object[] params;
      if (this.property == null) {
         params = new Object[]{arg};
      } else {
         params = new Object[]{this.property, arg};
      }

      try {
         this.setMethod(this.introspector.getMethod(clazz, "put", params));
      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         this.log.error("While looking for put('" + params[0] + "') method:", var6);
      }

   }

   public Object execute(Object o, Object value) throws IllegalAccessException, InvocationTargetException {
      if (this.isAlive()) {
         Object[] params;
         if (this.property == null) {
            params = new Object[]{value};
         } else {
            params = new Object[]{this.property, value};
         }

         return this.getMethod().invoke(o, params);
      } else {
         return null;
      }
   }
}
