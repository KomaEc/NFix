package soot.toolkits.graph;

import java.util.List;

public interface EdgeLabelledDirectedGraph<N, L> extends DirectedGraph<N> {
   List<L> getLabelsForEdges(N var1, N var2);

   DirectedGraph<N> getEdgesForLabel(L var1);

   boolean containsEdge(N var1, N var2, L var3);

   boolean containsAnyEdge(N var1, N var2);

   boolean containsAnyEdge(L var1);

   boolean containsNode(N var1);
}
