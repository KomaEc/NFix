package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.toolkits.scalar.ForwardFlowAnalysis;

class SimpleDominatorsAnalysis<N> extends ForwardFlowAnalysis<N, FlowSet<N>> {
   private FlowSet<N> emptySet;
   private BoundedFlowSet<N> fullSet;

   SimpleDominatorsAnalysis(DirectedGraph<N> graph) {
      super(graph);
      List<N> nodes = new ArrayList(graph.size());
      Iterator var3 = graph.iterator();

      while(var3.hasNext()) {
         N n = var3.next();
         nodes.add(n);
      }

      FlowUniverse<N> nodeUniverse = new CollectionFlowUniverse(nodes);
      this.emptySet = new ArrayPackedSet(nodeUniverse);
      this.fullSet = (BoundedFlowSet)this.emptySet.clone();
      this.fullSet.complement();
      this.doAnalysis();
   }

   protected FlowSet<N> newInitialFlow() {
      return this.fullSet.clone();
   }

   protected FlowSet<N> entryInitialFlow() {
      FlowSet<N> initSet = this.emptySet.clone();
      Iterator var2 = this.graph.getHeads().iterator();

      while(var2.hasNext()) {
         N h = var2.next();
         initSet.add(h);
      }

      return initSet;
   }

   protected void flowThrough(FlowSet<N> in, N block, FlowSet<N> out) {
      in.copy(out);
      out.add(block);
   }

   protected void merge(FlowSet<N> in1, FlowSet<N> in2, FlowSet<N> out) {
      in1.intersection(in2, out);
   }

   protected void mergeInto(N block, FlowSet<N> inout, FlowSet<N> in) {
      inout.intersection(in);
   }

   protected void copy(FlowSet<N> source, FlowSet<N> dest) {
      source.copy(dest);
   }
}
