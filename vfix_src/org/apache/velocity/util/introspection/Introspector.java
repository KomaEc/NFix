package org.apache.velocity.util.introspection;

import java.lang.reflect.Method;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;

public class Introspector extends IntrospectorBase {
   public static final String CACHEDUMP_MSG = "Introspector: detected classloader change. Dumping cache.";

   public Introspector(Log log) {
      super(log);
   }

   /** @deprecated */
   public Introspector(RuntimeLogger logger) {
      this((Log)(new RuntimeLoggerLog(logger)));
   }

   public Method getMethod(Class c, String name, Object[] params) throws IllegalArgumentException {
      try {
         return super.getMethod(c, name, params);
      } catch (MethodMap.AmbiguousException var7) {
         StringBuffer msg = (new StringBuffer("Introspection Error : Ambiguous method invocation ")).append(name).append("(");

         for(int i = 0; i < params.length; ++i) {
            if (i > 0) {
               msg.append(", ");
            }

            if (params[i] == null) {
               msg.append("null");
            } else {
               msg.append(params[i].getClass().getName());
            }
         }

         msg.append(") for class ").append(c);
         this.log.error(msg.toString());
         return null;
      }
   }

   public void triggerClear() {
      super.triggerClear();
      this.log.info("Introspector: detected classloader change. Dumping cache.");
   }
}
