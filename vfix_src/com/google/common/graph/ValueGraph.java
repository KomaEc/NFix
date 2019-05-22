package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface ValueGraph<N, V> extends BaseGraph<N> {
   Set<N> nodes();

   Set<EndpointPair<N>> edges();

   Graph<N> asGraph();

   boolean isDirected();

   boolean allowsSelfLoops();

   ElementOrder<N> nodeOrder();

   Set<N> adjacentNodes(N var1);

   Set<N> predecessors(N var1);

   Set<N> successors(N var1);

   int degree(N var1);

   int inDegree(N var1);

   int outDegree(N var1);

   boolean hasEdgeConnecting(N var1, N var2);

   Optional<V> edgeValue(N var1, N var2);

   @Nullable
   V edgeValueOrDefault(N var1, N var2, @Nullable V var3);

   boolean equals(@Nullable Object var1);

   int hashCode();
}
