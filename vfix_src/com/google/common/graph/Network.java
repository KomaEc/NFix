package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface Network<N, E> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
   Set<N> nodes();

   Set<E> edges();

   Graph<N> asGraph();

   boolean isDirected();

   boolean allowsParallelEdges();

   boolean allowsSelfLoops();

   ElementOrder<N> nodeOrder();

   ElementOrder<E> edgeOrder();

   Set<N> adjacentNodes(N var1);

   Set<N> predecessors(N var1);

   Set<N> successors(N var1);

   Set<E> incidentEdges(N var1);

   Set<E> inEdges(N var1);

   Set<E> outEdges(N var1);

   int degree(N var1);

   int inDegree(N var1);

   int outDegree(N var1);

   EndpointPair<N> incidentNodes(E var1);

   Set<E> adjacentEdges(E var1);

   Set<E> edgesConnecting(N var1, N var2);

   Optional<E> edgeConnecting(N var1, N var2);

   @Nullable
   E edgeConnectingOrNull(N var1, N var2);

   boolean hasEdgeConnecting(N var1, N var2);

   boolean equals(@Nullable Object var1);

   int hashCode();
}
