package soot.toolkits.scalar;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.interaction.InteractionHandler;

public abstract class AbstractFlowAnalysis<N, A> {
   protected Map<N, A> unitToBeforeFlow;
   protected Map<N, A> filterUnitToBeforeFlow = Collections.emptyMap();
   protected DirectedGraph<N> graph;

   public AbstractFlowAnalysis(DirectedGraph<N> graph) {
      this.unitToBeforeFlow = new IdentityHashMap(graph.size() * 2 + 1);
      this.graph = graph;
      if (Options.v().interactive_mode()) {
         InteractionHandler.v().handleCfgEvent(graph);
      }

   }

   protected abstract A newInitialFlow();

   protected A entryInitialFlow() {
      return this.newInitialFlow();
   }

   protected boolean treatTrapHandlersAsEntries() {
      return false;
   }

   protected abstract boolean isForward();

   protected abstract void merge(A var1, A var2, A var3);

   protected void merge(N succNode, A in1, A in2, A out) {
      this.merge(in1, in2, out);
   }

   protected abstract void copy(A var1, A var2);

   protected abstract void doAnalysis();

   public A getFlowBefore(N s) {
      return this.unitToBeforeFlow.get(s);
   }

   protected void mergeInto(N succNode, A inout, A in) {
      A tmp = this.newInitialFlow();
      this.merge(succNode, inout, in, tmp);
      this.copy(tmp, inout);
   }
}
