package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.lang.reflect.Method;
import java.util.Map;

public class ConvertedMap extends ConversionHandler {
   protected ConvertedMap(Map closures) {
      super(closures);
   }

   public Object invokeCustom(Object proxy, Method method, Object[] args) throws Throwable {
      Map m = (Map)this.getDelegate();
      Closure cl = (Closure)m.get(method.getName());
      if (cl == null) {
         throw new UnsupportedOperationException();
      } else {
         return cl.call(args);
      }
   }

   public String toString() {
      return DefaultGroovyMethods.toString(this.getDelegate());
   }

   protected boolean checkMethod(Method method) {
      return isCoreObjectMethod(method);
   }

   public static boolean isCoreObjectMethod(Method method) {
      return ConversionHandler.isCoreObjectMethod(method) && !"toString".equals(method.getName());
   }
}
