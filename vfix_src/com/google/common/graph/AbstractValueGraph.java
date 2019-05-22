package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractValueGraph<N, V> extends AbstractBaseGraph<N> implements ValueGraph<N, V> {
   public Graph<N> asGraph() {
      return new AbstractGraph<N>() {
         public Set<N> nodes() {
            return AbstractValueGraph.this.nodes();
         }

         public Set<EndpointPair<N>> edges() {
            return AbstractValueGraph.this.edges();
         }

         public boolean isDirected() {
            return AbstractValueGraph.this.isDirected();
         }

         public boolean allowsSelfLoops() {
            return AbstractValueGraph.this.allowsSelfLoops();
         }

         public ElementOrder<N> nodeOrder() {
            return AbstractValueGraph.this.nodeOrder();
         }

         public Set<N> adjacentNodes(N node) {
            return AbstractValueGraph.this.adjacentNodes(node);
         }

         public Set<N> predecessors(N node) {
            return AbstractValueGraph.this.predecessors(node);
         }

         public Set<N> successors(N node) {
            return AbstractValueGraph.this.successors(node);
         }

         public int degree(N node) {
            return AbstractValueGraph.this.degree(node);
         }

         public int inDegree(N node) {
            return AbstractValueGraph.this.inDegree(node);
         }

         public int outDegree(N node) {
            return AbstractValueGraph.this.outDegree(node);
         }
      };
   }

   public Optional<V> edgeValue(N nodeU, N nodeV) {
      return Optional.ofNullable(this.edgeValueOrDefault(nodeU, nodeV, (Object)null));
   }

   public final boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof ValueGraph)) {
         return false;
      } else {
         ValueGraph<?, ?> other = (ValueGraph)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeValueMap(this).equals(edgeValueMap(other));
      }
   }

   public final int hashCode() {
      return edgeValueMap(this).hashCode();
   }

   public String toString() {
      return "isDirected: " + this.isDirected() + ", allowsSelfLoops: " + this.allowsSelfLoops() + ", nodes: " + this.nodes() + ", edges: " + edgeValueMap(this);
   }

   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> graph) {
      Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>() {
         public V apply(EndpointPair<N> edge) {
            return graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), (Object)null);
         }
      };
      return Maps.asMap(graph.edges(), edgeToValueFn);
   }
}
