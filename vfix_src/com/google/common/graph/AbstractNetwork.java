package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractNetwork<N, E> implements Network<N, E> {
   public Graph<N> asGraph() {
      return new AbstractGraph<N>() {
         public Set<N> nodes() {
            return AbstractNetwork.this.nodes();
         }

         public Set<EndpointPair<N>> edges() {
            return (Set)(AbstractNetwork.this.allowsParallelEdges() ? super.edges() : new AbstractSet<EndpointPair<N>>() {
               public Iterator<EndpointPair<N>> iterator() {
                  return Iterators.transform(AbstractNetwork.this.edges().iterator(), new Function<E, EndpointPair<N>>() {
                     public EndpointPair<N> apply(E edge) {
                        return AbstractNetwork.this.incidentNodes(edge);
                     }
                  });
               }

               public int size() {
                  return AbstractNetwork.this.edges().size();
               }

               public boolean contains(@Nullable Object obj) {
                  if (!(obj instanceof EndpointPair)) {
                     return false;
                  } else {
                     EndpointPair<?> endpointPair = (EndpointPair)obj;
                     return isDirected() == endpointPair.isOrdered() && nodes().contains(endpointPair.nodeU()) && successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
                  }
               }
            });
         }

         public ElementOrder<N> nodeOrder() {
            return AbstractNetwork.this.nodeOrder();
         }

         public boolean isDirected() {
            return AbstractNetwork.this.isDirected();
         }

         public boolean allowsSelfLoops() {
            return AbstractNetwork.this.allowsSelfLoops();
         }

         public Set<N> adjacentNodes(N node) {
            return AbstractNetwork.this.adjacentNodes(node);
         }

         public Set<N> predecessors(N node) {
            return AbstractNetwork.this.predecessors(node);
         }

         public Set<N> successors(N node) {
            return AbstractNetwork.this.successors(node);
         }
      };
   }

   public int degree(N node) {
      return this.isDirected() ? IntMath.saturatedAdd(this.inEdges(node).size(), this.outEdges(node).size()) : IntMath.saturatedAdd(this.incidentEdges(node).size(), this.edgesConnecting(node, node).size());
   }

   public int inDegree(N node) {
      return this.isDirected() ? this.inEdges(node).size() : this.degree(node);
   }

   public int outDegree(N node) {
      return this.isDirected() ? this.outEdges(node).size() : this.degree(node);
   }

   public Set<E> adjacentEdges(E edge) {
      EndpointPair<N> endpointPair = this.incidentNodes(edge);
      Set<E> endpointPairIncidentEdges = Sets.union(this.incidentEdges(endpointPair.nodeU()), this.incidentEdges(endpointPair.nodeV()));
      return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
   }

   public Set<E> edgesConnecting(N nodeU, N nodeV) {
      Set<E> outEdgesU = this.outEdges(nodeU);
      Set<E> inEdgesV = this.inEdges(nodeV);
      return outEdgesU.size() <= inEdgesV.size() ? Collections.unmodifiableSet(Sets.filter(outEdgesU, this.connectedPredicate(nodeU, nodeV))) : Collections.unmodifiableSet(Sets.filter(inEdgesV, this.connectedPredicate(nodeV, nodeU)));
   }

   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
      return new Predicate<E>() {
         public boolean apply(E edge) {
            return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
         }
      };
   }

   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
      Set<E> edgesConnecting = this.edgesConnecting(nodeU, nodeV);
      switch(edgesConnecting.size()) {
      case 0:
         return Optional.empty();
      case 1:
         return Optional.of(edgesConnecting.iterator().next());
      default:
         throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", nodeU, nodeV));
      }
   }

   @Nullable
   public E edgeConnectingOrNull(N nodeU, N nodeV) {
      return this.edgeConnecting(nodeU, nodeV).orElse((Object)null);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      return !this.edgesConnecting(nodeU, nodeV).isEmpty();
   }

   public final boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Network)) {
         return false;
      } else {
         Network<?, ?> other = (Network)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other));
      }
   }

   public final int hashCode() {
      return edgeIncidentNodesMap(this).hashCode();
   }

   public String toString() {
      return "isDirected: " + this.isDirected() + ", allowsParallelEdges: " + this.allowsParallelEdges() + ", allowsSelfLoops: " + this.allowsSelfLoops() + ", nodes: " + this.nodes() + ", edges: " + edgeIncidentNodesMap(this);
   }

   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
      Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() {
         public EndpointPair<N> apply(E edge) {
            return network.incidentNodes(edge);
         }
      };
      return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
   }
}
