package groovy.util.slurpersupport;

import java.util.Iterator;

public abstract class NodeIterator implements Iterator {
   private static final Object DELAYED_INIT = new Object();
   private final Iterator iter;
   private Object nextNode;

   public NodeIterator(Iterator iter) {
      this.iter = iter;
      this.nextNode = DELAYED_INIT;
   }

   private void initNextNode() {
      if (this.nextNode == DELAYED_INIT) {
         this.nextNode = this.getNextNode(this.iter);
      }

   }

   public boolean hasNext() {
      this.initNextNode();
      return this.nextNode != null;
   }

   public Object next() {
      this.initNextNode();

      Object var1;
      try {
         var1 = this.nextNode;
      } finally {
         this.nextNode = this.getNextNode(this.iter);
      }

      return var1;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   protected abstract Object getNextNode(Iterator var1);
}
