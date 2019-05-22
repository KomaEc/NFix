package groovy.xml.streamingmarkupsupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Builder extends GroovyObjectSupport {
   protected final Map namespaceMethodMap = new HashMap();

   public Builder(Map namespaceMethodMap) {
      Iterator keyIterator = namespaceMethodMap.keySet().iterator();

      while(keyIterator.hasNext()) {
         Object key = keyIterator.next();
         List value = (List)namespaceMethodMap.get(key);
         Closure dg = ((Closure)value.get(1)).asWritable();
         this.namespaceMethodMap.put(key, new Object[]{value.get(0), dg, fettleMethodMap(dg, (Map)value.get(2))});
      }

   }

   private static Map fettleMethodMap(Closure defaultGenerator, Map methodMap) {
      Map newMethodMap = new HashMap();
      Iterator i$ = methodMap.keySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Object value = methodMap.get(o);
         if (value instanceof Closure) {
            newMethodMap.put(o, value);
         } else {
            newMethodMap.put(o, defaultGenerator.curry((Object[])((Object[])value)));
         }
      }

      return newMethodMap;
   }

   public abstract Object bind(Closure var1);

   protected abstract static class Built extends GroovyObjectSupport {
      protected final Closure root;
      protected final Map namespaceSpecificTags = new HashMap();

      public Built(Closure root, Map namespaceTagMap) {
         this.namespaceSpecificTags.putAll(namespaceTagMap);
         this.root = (Closure)root.clone();
         this.root.setDelegate(this);
      }
   }
}
