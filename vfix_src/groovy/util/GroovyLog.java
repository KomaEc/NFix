package groovy.util;

import groovy.lang.GroovyObjectSupport;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

/** @deprecated */
@Deprecated
public class GroovyLog extends GroovyObjectSupport {
   String prefix;

   public static GroovyLog newInstance(Class aClass) {
      return new GroovyLog(aClass);
   }

   public GroovyLog() {
      this("");
   }

   public GroovyLog(Class type) {
      this(type.getName());
   }

   public GroovyLog(Object obj) {
      this(obj.getClass());
   }

   public GroovyLog(String prefix) {
      this.prefix = prefix != null && prefix.length() > 0 ? "[" + prefix + ":" : "[";
   }

   public Object invokeMethod(String name, Object args) {
      if (args != null && args.getClass().isArray()) {
         args = DefaultGroovyMethods.join((Object[])((Object[])args), ",");
      }

      System.out.println(this.prefix + name + "] " + args);
      return null;
   }
}
