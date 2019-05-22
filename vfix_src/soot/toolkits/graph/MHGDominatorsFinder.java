package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MHGDominatorsFinder<N> implements DominatorsFinder<N> {
   protected DirectedGraph<N> graph;
   protected BitSet fullSet;
   protected List<N> heads;
   protected Map<N, BitSet> nodeToFlowSet;
   protected Map<N, Integer> nodeToIndex;
   protected Map<Integer, N> indexToNode;
   protected int lastIndex = 0;

   public MHGDominatorsFinder(DirectedGraph<N> graph) {
      this.graph = graph;
      this.doAnalysis();
   }

   protected void doAnalysis() {
      this.heads = this.graph.getHeads();
      this.nodeToFlowSet = new HashMap();
      this.nodeToIndex = new HashMap();
      this.indexToNode = new HashMap();
      this.fullSet = new BitSet(this.graph.size());
      this.fullSet.flip(0, this.graph.size());
      Iterator i = this.graph.iterator();

      while(i.hasNext()) {
         N o = i.next();
         if (this.heads.contains(o)) {
            BitSet self = new BitSet();
            self.set(this.indexOf(o));
            this.nodeToFlowSet.put(o, self);
         } else {
            this.nodeToFlowSet.put(o, this.fullSet);
         }
      }

      boolean changed = true;

      label42:
      do {
         changed = false;
         Iterator i = this.graph.iterator();

         while(true) {
            Object o;
            do {
               if (!i.hasNext()) {
                  continue label42;
               }

               o = i.next();
            } while(this.heads.contains(o));

            BitSet predsIntersect = (BitSet)this.fullSet.clone();
            Iterator j = this.graph.getPredsOf(o).iterator();

            while(j.hasNext()) {
               BitSet predSet = (BitSet)this.nodeToFlowSet.get(j.next());
               predsIntersect.and(predSet);
            }

            BitSet oldSet = (BitSet)this.nodeToFlowSet.get(o);
            predsIntersect.set(this.indexOf(o));
            if (!predsIntersect.equals(oldSet)) {
               this.nodeToFlowSet.put(o, predsIntersect);
               changed = true;
            }
         }
      } while(changed);

   }

   protected int indexOf(N o) {
      Integer index = (Integer)this.nodeToIndex.get(o);
      if (index == null) {
         index = this.lastIndex;
         this.nodeToIndex.put(o, index);
         this.indexToNode.put(index, o);
         ++this.lastIndex;
      }

      return index;
   }

   public DirectedGraph<N> getGraph() {
      return this.graph;
   }

   public List<N> getDominators(N node) {
      List<N> result = new ArrayList();
      BitSet bitSet = (BitSet)this.nodeToFlowSet.get(node);

      for(int i = 0; i < bitSet.length(); ++i) {
         if (bitSet.get(i)) {
            result.add(this.indexToNode.get(i));
         }
      }

      return result;
   }

   public N getImmediateDominator(N node) {
      if (this.getGraph().getHeads().contains(node)) {
         return null;
      } else {
         List<N> dominatorsList = this.getDominators(node);
         dominatorsList.remove(node);
         Iterator<N> dominatorsIt = dominatorsList.iterator();
         Object immediateDominator = null;

         while(immediateDominator == null && dominatorsIt.hasNext()) {
            N dominator = dominatorsIt.next();
            if (this.isDominatedByAll(dominator, dominatorsList)) {
               immediateDominator = dominator;
            }
         }

         return immediateDominator;
      }
   }

   public boolean isDominatedBy(N node, N dominator) {
      return this.getDominators(node).contains(dominator);
   }

   public boolean isDominatedByAll(N node, Collection<N> dominators) {
      return this.getDominators(node).containsAll(dominators);
   }
}
