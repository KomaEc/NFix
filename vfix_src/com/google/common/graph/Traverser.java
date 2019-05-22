package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

@Beta
public abstract class Traverser<N> {
   public static <N> Traverser<N> forGraph(SuccessorsFunction<N> graph) {
      Preconditions.checkNotNull(graph);
      return new Traverser.GraphTraverser(graph);
   }

   public static <N> Traverser<N> forTree(SuccessorsFunction<N> tree) {
      Preconditions.checkNotNull(tree);
      if (tree instanceof BaseGraph) {
         Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
      }

      if (tree instanceof Network) {
         Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
      }

      return new Traverser.TreeTraverser(tree);
   }

   public abstract Iterable<N> breadthFirst(N var1);

   public abstract Iterable<N> depthFirstPreOrder(N var1);

   public abstract Iterable<N> depthFirstPostOrder(N var1);

   private Traverser() {
   }

   // $FF: synthetic method
   Traverser(Object x0) {
      this();
   }

   private static enum Order {
      PREORDER,
      POSTORDER;
   }

   private static final class TreeTraverser<N> extends Traverser<N> {
      private final SuccessorsFunction<N> tree;

      TreeTraverser(SuccessorsFunction<N> tree) {
         super(null);
         this.tree = (SuccessorsFunction)Preconditions.checkNotNull(tree);
      }

      public Iterable<N> breadthFirst(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInTree(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return TreeTraverser.this.new BreadthFirstIterator(startNode);
            }
         };
      }

      public Iterable<N> depthFirstPreOrder(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInTree(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return TreeTraverser.this.new DepthFirstPreOrderIterator(startNode);
            }
         };
      }

      public Iterable<N> depthFirstPostOrder(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInTree(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return TreeTraverser.this.new DepthFirstPostOrderIterator(startNode);
            }
         };
      }

      private void checkThatNodeIsInTree(N startNode) {
         this.tree.successors(startNode);
      }

      private final class DepthFirstPostOrderIterator extends AbstractIterator<N> {
         private final ArrayDeque<Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren> stack = new ArrayDeque();

         DepthFirstPostOrderIterator(N root) {
            this.stack.addLast(this.withChildren(root));
         }

         protected N computeNext() {
            while(true) {
               if (!this.stack.isEmpty()) {
                  Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren top = (Traverser.TreeTraverser.DepthFirstPostOrderIterator.NodeAndChildren)this.stack.getLast();
                  if (top.childIterator.hasNext()) {
                     N child = top.childIterator.next();
                     this.stack.addLast(this.withChildren(child));
                     continue;
                  }

                  this.stack.removeLast();
                  return top.node;
               }

               return this.endOfData();
            }
         }

         Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren withChildren(N node) {
            return new Traverser.TreeTraverser.DepthFirstPostOrderIterator.NodeAndChildren(node, TreeTraverser.this.tree.successors(node));
         }

         private final class NodeAndChildren {
            final N node;
            final Iterator<? extends N> childIterator;

            NodeAndChildren(N node, Iterable<? extends N> children) {
               this.node = node;
               this.childIterator = children.iterator();
            }
         }
      }

      private final class DepthFirstPreOrderIterator extends UnmodifiableIterator<N> {
         private final Deque<Iterator<? extends N>> stack = new ArrayDeque();

         DepthFirstPreOrderIterator(N root) {
            this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
         }

         public boolean hasNext() {
            return !this.stack.isEmpty();
         }

         public N next() {
            Iterator<? extends N> iterator = (Iterator)this.stack.getLast();
            N result = Preconditions.checkNotNull(iterator.next());
            if (!iterator.hasNext()) {
               this.stack.removeLast();
            }

            Iterator<? extends N> childIterator = TreeTraverser.this.tree.successors(result).iterator();
            if (childIterator.hasNext()) {
               this.stack.addLast(childIterator);
            }

            return result;
         }
      }

      private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
         private final Queue<N> queue = new ArrayDeque();

         BreadthFirstIterator(N root) {
            this.queue.add(root);
         }

         public boolean hasNext() {
            return !this.queue.isEmpty();
         }

         public N next() {
            N current = this.queue.remove();
            Iterables.addAll(this.queue, TreeTraverser.this.tree.successors(current));
            return current;
         }
      }
   }

   private static final class GraphTraverser<N> extends Traverser<N> {
      private final SuccessorsFunction<N> graph;

      GraphTraverser(SuccessorsFunction<N> graph) {
         super(null);
         this.graph = (SuccessorsFunction)Preconditions.checkNotNull(graph);
      }

      public Iterable<N> breadthFirst(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInGraph(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return GraphTraverser.this.new BreadthFirstIterator(startNode);
            }
         };
      }

      public Iterable<N> depthFirstPreOrder(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInGraph(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return GraphTraverser.this.new DepthFirstIterator(startNode, Traverser.Order.PREORDER);
            }
         };
      }

      public Iterable<N> depthFirstPostOrder(final N startNode) {
         Preconditions.checkNotNull(startNode);
         this.checkThatNodeIsInGraph(startNode);
         return new Iterable<N>() {
            public Iterator<N> iterator() {
               return GraphTraverser.this.new DepthFirstIterator(startNode, Traverser.Order.POSTORDER);
            }
         };
      }

      private void checkThatNodeIsInGraph(N startNode) {
         this.graph.successors(startNode);
      }

      private final class DepthFirstIterator extends AbstractIterator<N> {
         private final Deque<Traverser.GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors> stack = new ArrayDeque();
         private final Set<N> visited = new HashSet();
         private final Traverser.Order order;

         DepthFirstIterator(N root, Traverser.Order order) {
            this.stack.push(this.withSuccessors(root));
            this.order = order;
         }

         protected N computeNext() {
            Traverser.GraphTraverser.DepthFirstIterator.NodeAndSuccessors node;
            boolean produceNode;
            do {
               if (this.stack.isEmpty()) {
                  return this.endOfData();
               }

               node = (Traverser.GraphTraverser.DepthFirstIterator.NodeAndSuccessors)this.stack.getFirst();
               boolean firstVisit = this.visited.add(node.node);
               boolean lastVisit = !node.successorIterator.hasNext();
               produceNode = firstVisit && this.order == Traverser.Order.PREORDER || lastVisit && this.order == Traverser.Order.POSTORDER;
               if (lastVisit) {
                  this.stack.pop();
               } else {
                  N successor = node.successorIterator.next();
                  if (!this.visited.contains(successor)) {
                     this.stack.push(this.withSuccessors(successor));
                  }
               }
            } while(!produceNode);

            return node.node;
         }

         Traverser.GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors withSuccessors(N node) {
            return new Traverser.GraphTraverser.DepthFirstIterator.NodeAndSuccessors(node, GraphTraverser.this.graph.successors(node));
         }

         private final class NodeAndSuccessors {
            final N node;
            final Iterator<? extends N> successorIterator;

            NodeAndSuccessors(N node, Iterable<? extends N> successors) {
               this.node = node;
               this.successorIterator = successors.iterator();
            }
         }
      }

      private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
         private final Queue<N> queue = new ArrayDeque();
         private final Set<N> visited = new HashSet();

         BreadthFirstIterator(N root) {
            this.queue.add(root);
            this.visited.add(root);
         }

         public boolean hasNext() {
            return !this.queue.isEmpty();
         }

         public N next() {
            N current = this.queue.remove();
            Iterator var2 = GraphTraverser.this.graph.successors(current).iterator();

            while(var2.hasNext()) {
               N neighbor = var2.next();
               if (this.visited.add(neighbor)) {
                  this.queue.add(neighbor);
               }
            }

            return current;
         }
      }
   }
}
