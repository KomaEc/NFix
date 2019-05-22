package soot.toolkits.graph;

public interface ReversibleGraph<N> extends MutableDirectedGraph<N> {
   boolean isReversed();

   ReversibleGraph<N> reverse();
}
