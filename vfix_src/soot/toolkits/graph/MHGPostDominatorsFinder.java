package soot.toolkits.graph;

public class MHGPostDominatorsFinder<N> extends MHGDominatorsFinder<N> {
   public MHGPostDominatorsFinder(DirectedGraph<N> graph) {
      super(new InverseGraph(graph));
   }
}
