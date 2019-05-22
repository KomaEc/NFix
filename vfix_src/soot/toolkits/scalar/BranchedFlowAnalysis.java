package soot.toolkits.scalar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;

public abstract class BranchedFlowAnalysis<N extends Unit, A> extends AbstractFlowAnalysis<N, A> {
   protected Map<Unit, List<A>> unitToAfterFallFlow;
   protected Map<Unit, List<A>> unitToAfterBranchFlow;

   public BranchedFlowAnalysis(DirectedGraph<N> graph) {
      super(graph);
      this.unitToAfterFallFlow = new HashMap(graph.size() * 2 + 1, 0.7F);
      this.unitToAfterBranchFlow = new HashMap(graph.size() * 2 + 1, 0.7F);
   }

   protected abstract void flowThrough(A var1, Unit var2, List<A> var3, List<A> var4);

   public A getFallFlowAfter(Unit s) {
      List<A> fl = (List)this.unitToAfterFallFlow.get(s);
      return fl.isEmpty() ? this.newInitialFlow() : fl.get(0);
   }

   public List<A> getBranchFlowAfter(Unit s) {
      return (List)this.unitToAfterBranchFlow.get(s);
   }
}
