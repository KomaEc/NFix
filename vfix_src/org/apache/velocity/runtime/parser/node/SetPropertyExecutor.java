package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.util.introspection.Introspector;

public class SetPropertyExecutor extends SetExecutor {
   private final Introspector introspector;

   public SetPropertyExecutor(Log log, Introspector introspector, Class clazz, String property, Object arg) {
      this.log = log;
      this.introspector = introspector;
      if (StringUtils.isNotEmpty(property)) {
         this.discover(clazz, property, arg);
      }

   }

   protected Introspector getIntrospector() {
      return this.introspector;
   }

   protected void discover(Class clazz, String property, Object arg) {
      Object[] params = new Object[]{arg};

      try {
         StringBuffer sb = new StringBuffer("set");
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
      } catch (RuntimeException var7) {
         throw var7;
      } catch (Exception var8) {
         this.log.error("While looking for property setter for '" + property + "':", var8);
      }

   }

   public Object execute(Object o, Object value) throws IllegalAccessException, InvocationTargetException {
      Object[] params = new Object[]{value};
      return this.isAlive() ? this.getMethod().invoke(o, params) : null;
   }
}
