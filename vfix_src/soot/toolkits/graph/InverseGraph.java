package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;

public class InverseGraph<N> implements DirectedGraph<N> {
   protected final DirectedGraph<N> g;

   public InverseGraph(DirectedGraph<N> g) {
      this.g = g;
   }

   public List<N> getHeads() {
      return this.g.getTails();
   }

   public List<N> getPredsOf(N s) {
      return this.g.getSuccsOf(s);
   }

   public List<N> getSuccsOf(N s) {
      return this.g.getPredsOf(s);
   }

   public List<N> getTails() {
      return this.g.getHeads();
   }

   public Iterator<N> iterator() {
      return this.g.iterator();
   }

   public int size() {
      return this.g.size();
   }
}
