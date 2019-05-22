package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

abstract class AbstractBaseGraph<N> implements BaseGraph<N> {
   protected long edgeCount() {
      long degreeSum = 0L;

      Object node;
      for(Iterator var3 = this.nodes().iterator(); var3.hasNext(); degreeSum += (long)this.degree(node)) {
         node = var3.next();
      }

      Preconditions.checkState((degreeSum & 1L) == 0L);
      return degreeSum >>> 1;
   }

   public Set<EndpointPair<N>> edges() {
      return new AbstractSet<EndpointPair<N>>() {
         public UnmodifiableIterator<EndpointPair<N>> iterator() {
            return EndpointPairIterator.of(AbstractBaseGraph.this);
         }

         public int size() {
            return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
         }

         public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof EndpointPair)) {
               return false;
            } else {
               EndpointPair<?> endpointPair = (EndpointPair)obj;
               return AbstractBaseGraph.this.isDirected() == endpointPair.isOrdered() && AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
         }
      };
   }

   public int degree(N node) {
      if (this.isDirected()) {
         return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
      } else {
         Set<N> neighbors = this.adjacentNodes(node);
         int selfLoopCount = this.allowsSelfLoops() && neighbors.contains(node) ? 1 : 0;
         return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
      }
   }

   public int inDegree(N node) {
      return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
   }

   public int outDegree(N node) {
      return this.isDirected() ? this.successors(node).size() : this.degree(node);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      Preconditions.checkNotNull(nodeU);
      Preconditions.checkNotNull(nodeV);
      return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
   }
}
