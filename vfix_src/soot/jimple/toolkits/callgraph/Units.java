package soot.jimple.toolkits.callgraph;

import java.util.Iterator;

public final class Units implements Iterator {
   Iterator edges;

   public Units(Iterator edges) {
      this.edges = edges;
   }

   public boolean hasNext() {
      return this.edges.hasNext();
   }

   public Object next() {
      Edge e = (Edge)this.edges.next();
      return e.srcUnit();
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
