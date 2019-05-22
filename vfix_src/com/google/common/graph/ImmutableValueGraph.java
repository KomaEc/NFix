package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;

@Immutable(
   containerOf = {"N", "V"}
)
@Beta
public final class ImmutableValueGraph<N, V> extends ConfigurableValueGraph<N, V> {
   private ImmutableValueGraph(ValueGraph<N, V> graph) {
      super(ValueGraphBuilder.from(graph), getNodeConnections(graph), (long)graph.edges().size());
   }

   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
      return graph instanceof ImmutableValueGraph ? (ImmutableValueGraph)graph : new ImmutableValueGraph(graph);
   }

   /** @deprecated */
   @Deprecated
   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
      return (ImmutableValueGraph)Preconditions.checkNotNull(graph);
   }

   public ImmutableGraph<N> asGraph() {
      return new ImmutableGraph(this);
   }

   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
      ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         nodeConnections.put(node, connectionsOf(graph, node));
      }

      return nodeConnections.build();
   }

   private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
      Function<N, V> successorNodeToValueFn = new Function<N, V>() {
         public V apply(N successorNode) {
            return graph.edgeValueOrDefault(node, successorNode, (Object)null);
         }
      };
      return (GraphConnections)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), Maps.asMap(graph.successors(node), successorNodeToValueFn)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn)));
   }
}
