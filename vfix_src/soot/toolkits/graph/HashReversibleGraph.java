package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;

public class HashReversibleGraph<N> extends HashMutableDirectedGraph<N> implements ReversibleGraph<N> {
   protected boolean reversed;

   public HashReversibleGraph(DirectedGraph<N> dg) {
      this();
      Iterator i = dg.iterator();

      Object s;
      while(i.hasNext()) {
         s = i.next();
         this.addNode(s);
      }

      i = dg.iterator();

      while(i.hasNext()) {
         s = i.next();
         List<N> succs = dg.getSuccsOf(s);
         Iterator succsIt = succs.iterator();

         while(succsIt.hasNext()) {
            N t = succsIt.next();
            this.addEdge(s, t);
         }
      }

      this.heads.clear();
      this.heads.addAll(dg.getHeads());
      this.tails.clear();
      this.tails.addAll(dg.getTails());
   }

   public HashReversibleGraph() {
      this.reversed = false;
   }

   public boolean isReversed() {
      return this.reversed;
   }

   public ReversibleGraph<N> reverse() {
      this.reversed = !this.reversed;
      return this;
   }

   public void addEdge(N from, N to) {
      if (this.reversed) {
         super.addEdge(to, from);
      } else {
         super.addEdge(from, to);
      }

   }

   public void removeEdge(N from, N to) {
      if (this.reversed) {
         super.removeEdge(to, from);
      } else {
         super.removeEdge(from, to);
      }

   }

   public boolean containsEdge(N from, N to) {
      return this.reversed ? super.containsEdge(to, from) : super.containsEdge(from, to);
   }

   public List<N> getHeads() {
      return this.reversed ? super.getTails() : super.getHeads();
   }

   public List<N> getTails() {
      return this.reversed ? super.getHeads() : super.getTails();
   }

   public List<N> getPredsOf(N s) {
      return this.reversed ? super.getSuccsOf(s) : super.getPredsOf(s);
   }

   public List<N> getSuccsOf(N s) {
      return this.reversed ? super.getPredsOf(s) : super.getSuccsOf(s);
   }
}
