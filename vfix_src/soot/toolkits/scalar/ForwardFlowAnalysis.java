package soot.toolkits.scalar;

import soot.Timers;
import soot.toolkits.graph.DirectedGraph;

public abstract class ForwardFlowAnalysis<N, A> extends FlowAnalysis<N, A> {
   public ForwardFlowAnalysis(DirectedGraph<N> graph) {
      super(graph);
   }

   protected boolean isForward() {
      return true;
   }

   protected void doAnalysis() {
      int i = this.doAnalysis(FlowAnalysis.GraphView.FORWARD, FlowAnalysis.InteractionFlowHandler.FORWARD, this.unitToBeforeFlow, this.unitToAfterFlow);
      Timers var10000 = Timers.v();
      var10000.totalFlowNodes += this.graph.size();
      var10000 = Timers.v();
      var10000.totalFlowComputations += i;
   }
}
