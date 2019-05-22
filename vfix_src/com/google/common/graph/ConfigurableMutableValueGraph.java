package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;

final class ConfigurableMutableValueGraph<N, V> extends ConfigurableValueGraph<N, V> implements MutableValueGraph<N, V> {
   ConfigurableMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
      super(builder);
   }

   @CanIgnoreReturnValue
   public boolean addNode(N node) {
      Preconditions.checkNotNull(node, "node");
      if (this.containsNode(node)) {
         return false;
      } else {
         this.addNodeInternal(node);
         return true;
      }
   }

   @CanIgnoreReturnValue
   private GraphConnections<N, V> addNodeInternal(N node) {
      GraphConnections<N, V> connections = this.newConnections();
      Preconditions.checkState(this.nodeConnections.put(node, connections) == null);
      return connections;
   }

   @CanIgnoreReturnValue
   public V putEdgeValue(N nodeU, N nodeV, V value) {
      Preconditions.checkNotNull(nodeU, "nodeU");
      Preconditions.checkNotNull(nodeV, "nodeV");
      Preconditions.checkNotNull(value, "value");
      if (!this.allowsSelfLoops()) {
         Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
      }

      GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
      if (connectionsU == null) {
         connectionsU = this.addNodeInternal(nodeU);
      }

      V previousValue = connectionsU.addSuccessor(nodeV, value);
      GraphConnections<N, V> connectionsV = (GraphConnections)this.nodeConnections.get(nodeV);
      if (connectionsV == null) {
         connectionsV = this.addNodeInternal(nodeV);
      }

      connectionsV.addPredecessor(nodeU, value);
      if (previousValue == null) {
         Graphs.checkPositive(++this.edgeCount);
      }

      return previousValue;
   }

   @CanIgnoreReturnValue
   public boolean removeNode(N node) {
      Preconditions.checkNotNull(node, "node");
      GraphConnections<N, V> connections = (GraphConnections)this.nodeConnections.get(node);
      if (connections == null) {
         return false;
      } else {
         if (this.allowsSelfLoops() && connections.removeSuccessor(node) != null) {
            connections.removePredecessor(node);
            --this.edgeCount;
         }

         Iterator var3;
         Object predecessor;
         for(var3 = connections.successors().iterator(); var3.hasNext(); --this.edgeCount) {
            predecessor = var3.next();
            ((GraphConnections)this.nodeConnections.getWithoutCaching(predecessor)).removePredecessor(node);
         }

         if (this.isDirected()) {
            for(var3 = connections.predecessors().iterator(); var3.hasNext(); --this.edgeCount) {
               predecessor = var3.next();
               Preconditions.checkState(((GraphConnections)this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null);
            }
         }

         this.nodeConnections.remove(node);
         Graphs.checkNonNegative(this.edgeCount);
         return true;
      }
   }

   @CanIgnoreReturnValue
   public V removeEdge(N nodeU, N nodeV) {
      Preconditions.checkNotNull(nodeU, "nodeU");
      Preconditions.checkNotNull(nodeV, "nodeV");
      GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
      GraphConnections<N, V> connectionsV = (GraphConnections)this.nodeConnections.get(nodeV);
      if (connectionsU != null && connectionsV != null) {
         V previousValue = connectionsU.removeSuccessor(nodeV);
         if (previousValue != null) {
            connectionsV.removePredecessor(nodeU);
            Graphs.checkNonNegative(--this.edgeCount);
         }

         return previousValue;
      } else {
         return null;
      }
   }

   private GraphConnections<N, V> newConnections() {
      return (GraphConnections)(this.isDirected() ? DirectedGraphConnections.of() : UndirectedGraphConnections.of());
   }
}
