package com.google.common.graph;

import java.util.Set;

abstract class ForwardingGraph<N> extends AbstractGraph<N> {
   protected abstract BaseGraph<N> delegate();

   public Set<N> nodes() {
      return this.delegate().nodes();
   }

   protected long edgeCount() {
      return (long)this.delegate().edges().size();
   }

   public boolean isDirected() {
      return this.delegate().isDirected();
   }

   public boolean allowsSelfLoops() {
      return this.delegate().allowsSelfLoops();
   }

   public ElementOrder<N> nodeOrder() {
      return this.delegate().nodeOrder();
   }

   public Set<N> adjacentNodes(N node) {
      return this.delegate().adjacentNodes(node);
   }

   public Set<N> predecessors(N node) {
      return this.delegate().predecessors(node);
   }

   public Set<N> successors(N node) {
      return this.delegate().successors(node);
   }

   public int degree(N node) {
      return this.delegate().degree(node);
   }

   public int inDegree(N node) {
      return this.delegate().inDegree(node);
   }

   public int outDegree(N node) {
      return this.delegate().outDegree(node);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      return this.delegate().hasEdgeConnecting(nodeU, nodeV);
   }
}
