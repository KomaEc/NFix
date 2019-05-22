package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.MethodOrMethodContext;

public final class Sources implements Iterator<MethodOrMethodContext> {
   Iterator<Edge> edges;

   public Sources(Iterator<Edge> edges) {
      this.edges = edges;
   }

   public boolean hasNext() {
      return this.edges.hasNext();
   }

   public MethodOrMethodContext next() {
      Edge e = (Edge)this.edges.next();
      return e.getSrc();
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
