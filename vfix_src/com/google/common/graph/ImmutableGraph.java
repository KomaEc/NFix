package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;

@Immutable(
   containerOf = {"N"}
)
@Beta
public class ImmutableGraph<N> extends ForwardingGraph<N> {
   private final BaseGraph<N> backingGraph;

   ImmutableGraph(BaseGraph<N> backingGraph) {
      this.backingGraph = backingGraph;
   }

   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
      return graph instanceof ImmutableGraph ? (ImmutableGraph)graph : new ImmutableGraph(new ConfigurableValueGraph(GraphBuilder.from(graph), getNodeConnections(graph), (long)graph.edges().size()));
   }

   /** @deprecated */
   @Deprecated
   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
      return (ImmutableGraph)Preconditions.checkNotNull(graph);
   }

   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
      ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         nodeConnections.put(node, connectionsOf(graph, node));
      }

      return nodeConnections.build();
   }

   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
      Function<Object, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
      return (GraphConnections)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), Maps.asMap(graph.successors(node), edgeValueFn)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), edgeValueFn)));
   }

   protected BaseGraph<N> delegate() {
      return this.backingGraph;
   }
}
