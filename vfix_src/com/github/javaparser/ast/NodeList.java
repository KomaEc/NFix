package com.github.javaparser.ast;

import com.github.javaparser.HasParentNode;
import com.github.javaparser.ast.observer.AstObserver;
import com.github.javaparser.ast.observer.Observable;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.InternalProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeList<N extends Node> implements List<N>, Iterable<N>, HasParentNode<NodeList<N>>, Visitable, Observable {
   @InternalProperty
   private List<N> innerList = new ArrayList(0);
   private Node parentNode;
   private List<AstObserver> observers = new ArrayList();

   public NodeList() {
      this.parentNode = null;
   }

   public NodeList(Collection<N> n) {
      this.addAll(n);
   }

   @SafeVarargs
   public NodeList(N... n) {
      this.addAll((Collection)Arrays.asList(n));
   }

   public boolean add(N node) {
      this.notifyElementAdded(this.innerList.size(), node);
      this.own(node);
      return this.innerList.add(node);
   }

   private void own(N node) {
      if (node != null) {
         this.setAsParentNodeOf(node);
      }
   }

   public boolean remove(Node node) {
      int index = this.innerList.indexOf(node);
      if (index != -1) {
         this.notifyElementRemoved(index, node);
         node.setParentNode((Node)null);
      }

      return this.innerList.remove(node);
   }

   public N removeFirst() {
      return this.remove(0);
   }

   public N removeLast() {
      return this.remove(this.innerList.size() - 1);
   }

   @SafeVarargs
   public static <X extends Node> NodeList<X> nodeList(X... nodes) {
      NodeList<X> nodeList = new NodeList();
      Collections.addAll(nodeList, nodes);
      return nodeList;
   }

   public static <X extends Node> NodeList<X> nodeList(Collection<X> nodes) {
      NodeList<X> nodeList = new NodeList();
      nodeList.addAll(nodes);
      return nodeList;
   }

   public static <X extends Node> NodeList<X> nodeList(NodeList<X> nodes) {
      NodeList<X> nodeList = new NodeList();
      nodeList.addAll(nodes);
      return nodeList;
   }

   public boolean contains(N node) {
      return this.innerList.contains(node);
   }

   public int size() {
      return this.innerList.size();
   }

   public N get(int i) {
      return (Node)this.innerList.get(i);
   }

   public Iterator<N> iterator() {
      return this.innerList.iterator();
   }

   public N set(int index, N element) {
      if (index >= 0 && index < this.innerList.size()) {
         if (element == this.innerList.get(index)) {
            return element;
         } else {
            this.notifyElementReplaced(index, element);
            ((Node)this.innerList.get(index)).setParentNode((Node)null);
            this.setAsParentNodeOf(element);
            return (Node)this.innerList.set(index, element);
         }
      } else {
         throw new IllegalArgumentException("Illegal index. The index should be between 0 and " + this.innerList.size() + " excluded. It is instead " + index);
      }
   }

   public N remove(int index) {
      this.notifyElementRemoved(index, (Node)this.innerList.get(index));
      N remove = (Node)this.innerList.remove(index);
      if (remove != null) {
         remove.setParentNode((Node)null);
      }

      return remove;
   }

   public boolean isEmpty() {
      return this.innerList.isEmpty();
   }

   public void sort(Comparator<? super N> comparator) {
      this.innerList.sort(comparator);
   }

   public void addAll(NodeList<N> otherList) {
      Iterator var2 = otherList.iterator();

      while(var2.hasNext()) {
         N node = (Node)var2.next();
         this.add(node);
      }

   }

   public void add(int index, N node) {
      this.notifyElementAdded(index, node);
      this.own(node);
      this.innerList.add(index, node);
   }

   public NodeList<N> addFirst(N node) {
      this.add(0, (Node)node);
      return this;
   }

   public NodeList<N> addLast(N node) {
      this.add(node);
      return this;
   }

   public NodeList<N> addAfter(N node, N afterThisNode) {
      int i = this.indexOf(afterThisNode);
      if (i == -1) {
         throw new IllegalArgumentException("Can't find node to insert after.");
      } else {
         this.add(i + 1, node);
         return this;
      }
   }

   public NodeList<N> addBefore(N node, N beforeThisNode) {
      int i = this.indexOf(beforeThisNode);
      if (i == -1) {
         throw new IllegalArgumentException("Can't find node to insert before.");
      } else {
         this.add(i, node);
         return this;
      }
   }

   public Optional<Node> getParentNode() {
      return Optional.ofNullable(this.parentNode);
   }

   public NodeList<N> setParentNode(Node parentNode) {
      this.parentNode = parentNode;
      this.setAsParentNodeOf(this.innerList);
      return this;
   }

   public Node getParentNodeForChildren() {
      return this.parentNode;
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public void forEach(Consumer<? super N> action) {
      this.innerList.forEach(action);
   }

   public boolean contains(Object o) {
      return this.innerList.contains(o);
   }

   public Object[] toArray() {
      return this.innerList.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return this.innerList.toArray(a);
   }

   public boolean remove(Object o) {
      return o instanceof Node ? this.remove((Node)o) : false;
   }

   public boolean containsAll(Collection<?> c) {
      return this.innerList.containsAll(c);
   }

   public boolean addAll(Collection<? extends N> c) {
      c.forEach(this::add);
      return !c.isEmpty();
   }

   public boolean addAll(int index, Collection<? extends N> c) {
      Iterator var3 = c.iterator();

      while(var3.hasNext()) {
         N e = (Node)var3.next();
         this.add(index++, e);
      }

      return !c.isEmpty();
   }

   public boolean removeAll(Collection<?> c) {
      boolean changed = false;

      Object e;
      for(Iterator var3 = c.iterator(); var3.hasNext(); changed = this.remove(e) || changed) {
         e = var3.next();
      }

      return changed;
   }

   public boolean retainAll(Collection<?> c) {
      boolean changed = false;
      Object[] var3 = this.stream().filter((it) -> {
         return !c.contains(it);
      }).toArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object e = var3[var5];
         if (!c.contains(e)) {
            changed = this.remove(e) || changed;
         }
      }

      return changed;
   }

   public void replaceAll(UnaryOperator<N> operator) {
      for(int i = 0; i < this.size(); ++i) {
         this.set(i, (Node)operator.apply(this.get(i)));
      }

   }

   public boolean removeIf(Predicate<? super N> filter) {
      boolean changed = false;
      Object[] var3 = this.stream().filter(filter).toArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object e = var3[var5];
         changed = this.remove(e) || changed;
      }

      return changed;
   }

   public void clear() {
      while(!this.isEmpty()) {
         this.remove(0);
      }

   }

   public boolean equals(Object o) {
      return this.innerList.equals(o);
   }

   public int hashCode() {
      return this.innerList.hashCode();
   }

   public int indexOf(Object o) {
      return this.innerList.indexOf(o);
   }

   public int lastIndexOf(Object o) {
      return this.innerList.lastIndexOf(o);
   }

   public ListIterator<N> listIterator() {
      return this.innerList.listIterator();
   }

   public ListIterator<N> listIterator(int index) {
      return this.innerList.listIterator(index);
   }

   public Stream<N> parallelStream() {
      return this.innerList.parallelStream();
   }

   public List<N> subList(int fromIndex, int toIndex) {
      return this.innerList.subList(fromIndex, toIndex);
   }

   public Spliterator<N> spliterator() {
      return this.innerList.spliterator();
   }

   private void notifyElementAdded(int index, Node nodeAddedOrRemoved) {
      this.observers.forEach((o) -> {
         o.listChange(this, AstObserver.ListChangeType.ADDITION, index, nodeAddedOrRemoved);
      });
   }

   private void notifyElementRemoved(int index, Node nodeAddedOrRemoved) {
      this.observers.forEach((o) -> {
         o.listChange(this, AstObserver.ListChangeType.REMOVAL, index, nodeAddedOrRemoved);
      });
   }

   private void notifyElementReplaced(int index, Node nodeAddedOrRemoved) {
      this.observers.forEach((o) -> {
         o.listReplacement(this, index, this.get(index), nodeAddedOrRemoved);
      });
   }

   public void unregister(AstObserver observer) {
      this.observers.remove(observer);
   }

   public void register(AstObserver observer) {
      this.observers.add(observer);
   }

   public boolean isRegistered(AstObserver observer) {
      return this.observers.contains(observer);
   }

   public boolean replace(N old, N replacement) {
      int i = this.indexOf(old);
      if (i == -1) {
         return false;
      } else {
         this.set(i, replacement);
         return true;
      }
   }

   public boolean isNonEmpty() {
      return !this.isEmpty();
   }

   public void ifNonEmpty(Consumer<? super NodeList<N>> consumer) {
      if (this.isNonEmpty()) {
         consumer.accept(this);
      }

   }

   public static <T extends Node> Collector<T, NodeList<T>, NodeList<T>> toNodeList() {
      return Collector.of(NodeList::new, NodeList::add, (left, right) -> {
         left.addAll(right);
         return left;
      });
   }

   private void setAsParentNodeOf(List<? extends Node> childNodes) {
      if (childNodes != null) {
         Iterator var2 = childNodes.iterator();

         while(var2.hasNext()) {
            HasParentNode current = (HasParentNode)var2.next();
            current.setParentNode(this.getParentNodeForChildren());
         }
      }

   }

   private void setAsParentNodeOf(Node childNode) {
      if (childNode != null) {
         childNode.setParentNode(this.getParentNodeForChildren());
      }

   }

   public String toString() {
      return (String)this.innerList.stream().map(Node::toString).collect(Collectors.joining(", ", "[", "]"));
   }
}
