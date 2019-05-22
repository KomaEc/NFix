package soot.toolkits.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.toolkits.scalar.FlowSet;

public class SimpleDominatorsFinder<N> implements DominatorsFinder<N> {
   private static final Logger logger = LoggerFactory.getLogger(SimpleDominatorsFinder.class);
   protected DirectedGraph<N> graph;
   protected Map<N, FlowSet<N>> nodeToDominators;

   public SimpleDominatorsFinder(DirectedGraph<N> graph) {
      this.graph = graph;
      SimpleDominatorsAnalysis<N> analysis = new SimpleDominatorsAnalysis(graph);
      this.nodeToDominators = new HashMap(graph.size() * 2 + 1, 0.7F);
      Iterator nodeIt = graph.iterator();

      while(nodeIt.hasNext()) {
         N node = nodeIt.next();
         FlowSet<N> set = (FlowSet)analysis.getFlowAfter(node);
         this.nodeToDominators.put(node, set);
      }

   }

   public DirectedGraph<N> getGraph() {
      return this.graph;
   }

   public List<N> getDominators(N node) {
      return ((FlowSet)this.nodeToDominators.get(node)).toList();
   }

   public N getImmediateDominator(N node) {
      if (this.getGraph().getHeads().contains(node)) {
         return null;
      } else {
         FlowSet<N> head = ((FlowSet)this.nodeToDominators.get(node)).clone();
         head.remove(node);
         Iterator var3 = head.iterator();

         Object dominator;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            dominator = var3.next();
         } while(!((FlowSet)this.nodeToDominators.get(dominator)).isSubSet(head));

         return dominator;
      }
   }

   public boolean isDominatedBy(N node, N dominator) {
      return ((FlowSet)this.nodeToDominators.get(node)).contains(dominator);
   }

   public boolean isDominatedByAll(N node, Collection<N> dominators) {
      FlowSet<N> f = (FlowSet)this.nodeToDominators.get(node);
      Iterator var4 = dominators.iterator();

      Object n;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         n = var4.next();
      } while(f.contains(n));

      return false;
   }
}
