package soot.toolkits.graph;

import java.util.List;

public interface MutableDirectedGraph<N> extends DirectedGraph<N> {
   void addEdge(N var1, N var2);

   void removeEdge(N var1, N var2);

   boolean containsEdge(N var1, N var2);

   List<N> getNodes();

   void addNode(N var1);

   void removeNode(N var1);

   boolean containsNode(N var1);
}
