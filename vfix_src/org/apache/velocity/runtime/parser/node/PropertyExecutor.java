package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;

public class PropertyExecutor extends AbstractExecutor {
   private final Introspector introspector;

   public PropertyExecutor(Log log, Introspector introspector, Class clazz, String property) {
      this.log = log;
      this.introspector = introspector;
      if (StringUtils.isNotEmpty(property)) {
         this.discover(clazz, property);
      }

   }

   /** @deprecated */
   public PropertyExecutor(RuntimeLogger r, Introspector introspector, Class clazz, String property) {
      this((Log)(new RuntimeLoggerLog(r)), introspector, clazz, property);
   }

   protected Introspector getIntrospector() {
      return this.introspector;
   }

   protected void discover(Class clazz, String property) {
      try {
         Object[] params = new Object[0];
         StringBuffer sb = new StringBuffer("get");
         sb.append(property);
         this.setMethod(this.introspector.getMethod(clazz, sb.toString(), params));
         if (!this.isAlive()) {
            char c = sb.charAt(3);
            if (Character.isLowerCase(c)) {
               sb.setCharAt(3, Character.toUpperCase(c));
            } else {
               sb.setCharAt(3, Character.toLowerCase(c));
            }

            this.setMethod(this.introspector.getMethod(clazz, sb.toString(), params));
         }
      } catch (RuntimeException var6) {
         throw var6;
      } catch (Exception var7) {
         this.log.error("While looking for property getter for '" + property + "':", var7);
      }

   }

   public Object execute(Object o) throws IllegalAccessException, InvocationTargetException {
      return this.isAlive() ? this.getMethod().invoke(o, (Object[])null) : null;
   }
}
