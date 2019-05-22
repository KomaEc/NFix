package groovy.util.slurpersupport;

import groovy.lang.Closure;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class FilteredAttributes extends Attributes {
   private final Closure closure;

   public FilteredAttributes(GPathResult parent, Closure closure, Map<String, String> namespaceTagHints) {
      super(parent, parent.name, namespaceTagHints);
      this.closure = closure;
   }

   public Iterator nodeIterator() {
      return new NodeIterator(this.parent.iterator()) {
         protected Object getNextNode(Iterator iter) {
            while(true) {
               if (iter.hasNext()) {
                  Object node = iter.next();
                  if (!DefaultTypeTransformation.castToBoolean(FilteredAttributes.this.closure.call(new Object[]{node}))) {
                     continue;
                  }

                  return node;
               }

               return null;
            }
         }
      };
   }
}
