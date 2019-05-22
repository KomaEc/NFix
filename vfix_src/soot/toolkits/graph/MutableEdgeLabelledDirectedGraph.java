package soot.toolkits.graph;

public interface MutableEdgeLabelledDirectedGraph<N, L> extends EdgeLabelledDirectedGraph<N, L> {
   void addEdge(N var1, N var2, L var3);

   void removeEdge(N var1, N var2, L var3);

   void removeAllEdges(N var1, N var2);

   void removeAllEdges(L var1);

   void addNode(N var1);

   void removeNode(N var1);
}
