package soot.toolkits.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DominatorTreeAdapter<N> implements DirectedGraph<DominatorNode<N>> {
   DominatorTree<N> dt;

   public DominatorTreeAdapter(DominatorTree<N> dt) {
      this.dt = dt;
   }

   public List<DominatorNode<N>> getHeads() {
      return this.dt.getHeads();
   }

   public List<DominatorNode<N>> getTails() {
      return this.dt.getTails();
   }

   public List<DominatorNode<N>> getPredsOf(DominatorNode<N> node) {
      return Collections.singletonList(this.dt.getParentOf(node));
   }

   public List<DominatorNode<N>> getSuccsOf(DominatorNode<N> node) {
      return this.dt.getChildrenOf(node);
   }

   public Iterator<DominatorNode<N>> iterator() {
      return this.dt.iterator();
   }

   public int size() {
      return this.dt.size();
   }
}
