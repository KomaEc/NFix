package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

final class DirectedMultiNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E> {
   @LazyInit
   private transient Reference<Multiset<N>> predecessorsReference;
   @LazyInit
   private transient Reference<Multiset<N>> successorsReference;

   private DirectedMultiNetworkConnections(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
      super(inEdges, outEdges, selfLoopCount);
   }

   static <N, E> DirectedMultiNetworkConnections<N, E> of() {
      return new DirectedMultiNetworkConnections(new HashMap(2, 1.0F), new HashMap(2, 1.0F), 0);
   }

   static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
      return new DirectedMultiNetworkConnections(ImmutableMap.copyOf(inEdges), ImmutableMap.copyOf(outEdges), selfLoopCount);
   }

   public Set<N> predecessors() {
      return Collections.unmodifiableSet(this.predecessorsMultiset().elementSet());
   }

   private Multiset<N> predecessorsMultiset() {
      Multiset<N> predecessors = (Multiset)getReference(this.predecessorsReference);
      if (predecessors == null) {
         predecessors = HashMultiset.create(this.inEdgeMap.values());
         this.predecessorsReference = new SoftReference(predecessors);
      }

      return (Multiset)predecessors;
   }

   public Set<N> successors() {
      return Collections.unmodifiableSet(this.successorsMultiset().elementSet());
   }

   private Multiset<N> successorsMultiset() {
      Multiset<N> successors = (Multiset)getReference(this.successorsReference);
      if (successors == null) {
         successors = HashMultiset.create(this.outEdgeMap.values());
         this.successorsReference = new SoftReference(successors);
      }

      return (Multiset)successors;
   }

   public Set<E> edgesConnecting(final N node) {
      return new MultiEdgesConnecting<E>(this.outEdgeMap, node) {
         public int size() {
            return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
         }
      };
   }

   public N removeInEdge(E edge, boolean isSelfLoop) {
      N node = super.removeInEdge(edge, isSelfLoop);
      Multiset<N> predecessors = (Multiset)getReference(this.predecessorsReference);
      if (predecessors != null) {
         Preconditions.checkState(predecessors.remove(node));
      }

      return node;
   }

   public N removeOutEdge(E edge) {
      N node = super.removeOutEdge(edge);
      Multiset<N> successors = (Multiset)getReference(this.successorsReference);
      if (successors != null) {
         Preconditions.checkState(successors.remove(node));
      }

      return node;
   }

   public void addInEdge(E edge, N node, boolean isSelfLoop) {
      super.addInEdge(edge, node, isSelfLoop);
      Multiset<N> predecessors = (Multiset)getReference(this.predecessorsReference);
      if (predecessors != null) {
         Preconditions.checkState(predecessors.add(node));
      }

   }

   public void addOutEdge(E edge, N node) {
      super.addOutEdge(edge, node);
      Multiset<N> successors = (Multiset)getReference(this.successorsReference);
      if (successors != null) {
         Preconditions.checkState(successors.add(node));
      }

   }

   @Nullable
   private static <T> T getReference(@Nullable Reference<T> reference) {
      return reference == null ? null : reference.get();
   }
}
