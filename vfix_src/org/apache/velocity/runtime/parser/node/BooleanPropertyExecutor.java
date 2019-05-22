package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.Method;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;

public class BooleanPropertyExecutor extends PropertyExecutor {
   public BooleanPropertyExecutor(Log log, Introspector introspector, Class clazz, String property) {
      super(log, introspector, clazz, property);
   }

   /** @deprecated */
   public BooleanPropertyExecutor(RuntimeLogger rlog, Introspector introspector, Class clazz, String property) {
      super((Log)(new RuntimeLoggerLog(rlog)), introspector, clazz, property);
   }

   protected void discover(Class clazz, String property) {
      try {
         Object[] params = new Object[0];
         StringBuffer sb = new StringBuffer("is");
         sb.append(property);
         this.setMethod(this.getIntrospector().getMethod(clazz, sb.toString(), params));
         if (!this.isAlive()) {
            char c = sb.charAt(2);
            if (Character.isLowerCase(c)) {
               sb.setCharAt(2, Character.toUpperCase(c));
            } else {
               sb.setCharAt(2, Character.toLowerCase(c));
            }

            this.setMethod(this.getIntrospector().getMethod(clazz, sb.toString(), params));
         }

         if (this.isAlive() && this.getMethod().getReturnType() != Boolean.TYPE) {
            this.setMethod((Method)null);
         }
      } catch (RuntimeException var6) {
         throw var6;
      } catch (Exception var7) {
         this.log.error("While looking for boolean property getter for '" + property + "':", var7);
      }

   }
}
