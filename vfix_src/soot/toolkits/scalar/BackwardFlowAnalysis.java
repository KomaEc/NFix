package soot.toolkits.scalar;

import soot.toolkits.graph.DirectedGraph;

public abstract class BackwardFlowAnalysis<N, A> extends FlowAnalysis<N, A> {
   public BackwardFlowAnalysis(DirectedGraph<N> graph) {
      super(graph);
   }

   protected boolean isForward() {
      return false;
   }

   protected void doAnalysis() {
      this.doAnalysis(FlowAnalysis.GraphView.BACKWARD, FlowAnalysis.InteractionFlowHandler.BACKWARD, this.unitToAfterFlow, this.unitToBeforeFlow);
   }
}
