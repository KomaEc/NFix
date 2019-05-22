package groovy.util.slurpersupport;

import groovy.lang.Closure;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class FilteredNodeChildren extends NodeChildren {
   private final Closure closure;

   public FilteredNodeChildren(GPathResult parent, Closure closure, Map<String, String> namespaceTagHints) {
      super(parent, parent.name, namespaceTagHints);
      this.closure = closure;
   }

   public Iterator iterator() {
      return new Iterator() {
         final Iterator iter;
         Object next;

         {
            this.iter = FilteredNodeChildren.this.parent.iterator();
            this.next = null;
         }

         public boolean hasNext() {
            while(true) {
               if (this.iter.hasNext()) {
                  Object childNode = this.iter.next();
                  if (!FilteredNodeChildren.this.closureYieldsTrueForNode(childNode)) {
                     continue;
                  }

                  this.next = childNode;
                  return true;
               }

               return false;
            }
         }

         public Object next() {
            return this.next;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Iterator nodeIterator() {
      return new NodeIterator(this.parent.nodeIterator()) {
         protected Object getNextNode(Iterator iter) {
            while(true) {
               if (iter.hasNext()) {
                  Object node = iter.next();
                  if (!FilteredNodeChildren.this.closureYieldsTrueForNode(new NodeChild((Node)node, FilteredNodeChildren.this.parent, FilteredNodeChildren.this.namespaceTagHints))) {
                     continue;
                  }

                  return node;
               }

               return null;
            }
         }
      };
   }

   private boolean closureYieldsTrueForNode(Object childNode) {
      return DefaultTypeTransformation.castToBoolean(this.closure.call(new Object[]{childNode}));
   }
}
