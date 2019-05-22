package com.github.javaparser.ast;

import com.github.javaparser.HasParentNode;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.observer.AstObserver;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.observer.PropagatingAstObserver;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.EqualsVisitor;
import com.github.javaparser.ast.visitor.HashCodeVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.metamodel.InternalProperty;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Node implements Cloneable, HasParentNode<Node>, Visitable, NodeWithRange<Node>, NodeWithTokenRange<Node> {
   public static Comparator<NodeWithRange<?>> NODE_BY_BEGIN_POSITION = (a, b) -> {
      if (a.getRange().isPresent() && b.getRange().isPresent()) {
         return ((Range)a.getRange().get()).begin.compareTo(((Range)b.getRange().get()).begin);
      } else if (!a.getRange().isPresent() && !b.getRange().isPresent()) {
         return 0;
      } else {
         return a.getRange().isPresent() ? 1 : -1;
      }
   };
   private static final PrettyPrinter toStringPrinter = new PrettyPrinter(new PrettyPrinterConfiguration());
   protected static final PrettyPrinterConfiguration prettyPrinterNoCommentsConfiguration = (new PrettyPrinterConfiguration()).setPrintComments(false);
   @InternalProperty
   private Range range;
   @InternalProperty
   private TokenRange tokenRange;
   @InternalProperty
   private Node parentNode;
   @InternalProperty
   private List<Node> childNodes = new LinkedList();
   @InternalProperty
   private List<Comment> orphanComments = new LinkedList();
   @InternalProperty
   private IdentityHashMap<DataKey<?>, Object> data = null;
   @OptionalProperty
   private Comment comment;
   @InternalProperty
   private List<AstObserver> observers = new ArrayList();
   @InternalProperty
   private Node.Parsedness parsed;
   public static final int ABSOLUTE_BEGIN_LINE = -1;
   public static final int ABSOLUTE_END_LINE = -2;
   public static final DataKey<SymbolResolver> SYMBOL_RESOLVER_KEY = new DataKey<SymbolResolver>() {
   };

   protected Node(TokenRange tokenRange) {
      this.parsed = Node.Parsedness.PARSED;
      this.setTokenRange(tokenRange);
   }

   protected void customInitialization() {
   }

   public Optional<Comment> getComment() {
      return Optional.ofNullable(this.comment);
   }

   public Optional<Range> getRange() {
      return Optional.ofNullable(this.range);
   }

   public Optional<TokenRange> getTokenRange() {
      return Optional.ofNullable(this.tokenRange);
   }

   public Node setTokenRange(TokenRange tokenRange) {
      this.tokenRange = tokenRange;
      if (tokenRange != null && tokenRange.getBegin().getRange().isPresent() && tokenRange.getBegin().getRange().isPresent()) {
         this.range = new Range(((Range)tokenRange.getBegin().getRange().get()).begin, ((Range)tokenRange.getEnd().getRange().get()).end);
      } else {
         this.range = null;
      }

      return this;
   }

   public Node setRange(Range range) {
      if (this.range == range) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.RANGE, this.range, range);
         this.range = range;
         return this;
      }
   }

   public final Node setComment(final Comment comment) {
      if (this.comment == comment) {
         return this;
      } else if (comment != null && this instanceof Comment) {
         throw new RuntimeException("A comment can not be commented");
      } else {
         this.notifyPropertyChange(ObservableProperty.COMMENT, this.comment, comment);
         if (this.comment != null) {
            this.comment.setCommentedNode((Node)null);
         }

         this.comment = comment;
         if (comment != null) {
            this.comment.setCommentedNode(this);
         }

         return this;
      }
   }

   public final Node setLineComment(String comment) {
      return this.setComment(new LineComment(comment));
   }

   public final Node setBlockComment(String comment) {
      return this.setComment(new BlockComment(comment));
   }

   public final String toString() {
      return toStringPrinter.print(this);
   }

   public final String toString(PrettyPrinterConfiguration prettyPrinterConfiguration) {
      return (new PrettyPrinter(prettyPrinterConfiguration)).print(this);
   }

   public final int hashCode() {
      return HashCodeVisitor.hashCode(this);
   }

   public boolean equals(final Object obj) {
      return obj != null && obj instanceof Node ? EqualsVisitor.equals(this, (Node)obj) : false;
   }

   public Optional<Node> getParentNode() {
      return Optional.ofNullable(this.parentNode);
   }

   public List<Node> getChildNodes() {
      return Collections.unmodifiableList(this.childNodes);
   }

   public void addOrphanComment(Comment comment) {
      this.orphanComments.add(comment);
      comment.setParentNode(this);
   }

   public boolean removeOrphanComment(Comment comment) {
      boolean removed = this.orphanComments.remove(comment);
      if (removed) {
         comment.setParentNode((Node)null);
      }

      return removed;
   }

   public List<Comment> getOrphanComments() {
      return new LinkedList(this.orphanComments);
   }

   public List<Comment> getAllContainedComments() {
      List<Comment> comments = new LinkedList();
      comments.addAll(this.getOrphanComments());
      Iterator var2 = this.getChildNodes().iterator();

      while(var2.hasNext()) {
         Node child = (Node)var2.next();
         child.getComment().ifPresent(comments::add);
         comments.addAll(child.getAllContainedComments());
      }

      return comments;
   }

   public Node setParentNode(Node newParentNode) {
      if (newParentNode == this.parentNode) {
         return this;
      } else {
         this.observers.forEach((o) -> {
            o.parentChange(this, this.parentNode, newParentNode);
         });
         if (this.parentNode != null) {
            List<Node> parentChildNodes = this.parentNode.childNodes;

            for(int i = 0; i < parentChildNodes.size(); ++i) {
               if (parentChildNodes.get(i) == this) {
                  parentChildNodes.remove(i);
               }
            }
         }

         this.parentNode = newParentNode;
         if (this.parentNode != null) {
            this.parentNode.childNodes.add(this);
         }

         return this;
      }
   }

   protected void setAsParentNodeOf(Node childNode) {
      if (childNode != null) {
         childNode.setParentNode(this.getParentNodeForChildren());
      }

   }

   /** @deprecated */
   @Deprecated
   public boolean hasComment() {
      return this.comment != null;
   }

   public void tryAddImportToParentCompilationUnit(Class<?> clazz) {
      this.findAncestor(CompilationUnit.class).ifPresent((p) -> {
         p.addImport(clazz);
      });
   }

   /** @deprecated */
   @Deprecated
   public <N extends Node> List<N> getChildNodesByType(Class<N> clazz) {
      List<N> nodes = new ArrayList();

      Node child;
      for(Iterator var3 = this.getChildNodes().iterator(); var3.hasNext(); nodes.addAll(child.getChildNodesByType(clazz))) {
         child = (Node)var3.next();
         if (clazz.isInstance(child)) {
            nodes.add(clazz.cast(child));
         }
      }

      return nodes;
   }

   /** @deprecated */
   @Deprecated
   public <N extends Node> List<N> getNodesByType(Class<N> clazz) {
      return this.getChildNodesByType(clazz);
   }

   public <M> M getData(final DataKey<M> key) {
      if (this.data == null) {
         throw new IllegalStateException("No data of this type found. Use containsData to check for this first.");
      } else {
         M value = this.data.get(key);
         if (value == null) {
            throw new IllegalStateException("No data of this type found. Use containsData to check for this first.");
         } else {
            return value;
         }
      }
   }

   public <M> void setData(DataKey<M> key, M object) {
      if (this.data == null) {
         this.data = new IdentityHashMap();
      }

      this.data.put(key, object);
   }

   public boolean containsData(DataKey<?> key) {
      return this.data == null ? false : this.data.containsKey(key);
   }

   public void removeData(DataKey<ResolvedType> key) {
      if (this.data != null) {
         this.data.remove(key);
      }

   }

   public boolean remove() {
      return this.parentNode == null ? false : this.parentNode.remove(this);
   }

   public boolean replace(Node node) {
      return this.parentNode == null ? false : this.parentNode.replace(this, node);
   }

   public void removeForced() {
      if (!this.remove()) {
         this.getParentNode().ifPresent(Node::remove);
      }

   }

   public Node getParentNodeForChildren() {
      return this;
   }

   protected void setAsParentNodeOf(NodeList<? extends Node> list) {
      if (list != null) {
         list.setParentNode(this.getParentNodeForChildren());
      }

   }

   public <P> void notifyPropertyChange(ObservableProperty property, P oldValue, P newValue) {
      this.observers.forEach((o) -> {
         o.propertyChange(this, property, oldValue, newValue);
      });
   }

   public void unregister(AstObserver observer) {
      this.observers.remove(observer);
   }

   public void register(AstObserver observer) {
      this.observers.add(observer);
   }

   public void register(AstObserver observer, Node.ObserverRegistrationMode mode) {
      if (mode == null) {
         throw new IllegalArgumentException("Mode should be not null");
      } else {
         switch(mode) {
         case JUST_THIS_NODE:
            this.register(observer);
            break;
         case THIS_NODE_AND_EXISTING_DESCENDANTS:
            this.registerForSubtree(observer);
            break;
         case SELF_PROPAGATING:
            this.registerForSubtree(PropagatingAstObserver.transformInPropagatingObserver(observer));
            break;
         default:
            throw new UnsupportedOperationException("This mode is not supported: " + mode);
         }

      }
   }

   public void registerForSubtree(AstObserver observer) {
      this.register(observer);
      this.getChildNodes().forEach((c) -> {
         c.registerForSubtree(observer);
      });
      Iterator var2 = this.getMetaModel().getAllPropertyMetaModels().iterator();

      while(var2.hasNext()) {
         PropertyMetaModel property = (PropertyMetaModel)var2.next();
         if (property.isNodeList()) {
            NodeList<?> nodeList = (NodeList)property.getValue(this);
            if (nodeList != null) {
               nodeList.register(observer);
            }
         }
      }

   }

   public boolean isRegistered(AstObserver observer) {
      return this.observers.contains(observer);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.comment != null && node == this.comment) {
         this.removeComment();
         return true;
      } else {
         return false;
      }
   }

   public Node removeComment() {
      return this.setComment((Comment)null);
   }

   public Node clone() {
      return (Node)this.accept(new CloneVisitor(), (Object)null);
   }

   public NodeMetaModel getMetaModel() {
      return JavaParserMetaModel.nodeMetaModel;
   }

   public Node.Parsedness getParsed() {
      return this.parsed;
   }

   public Node setParsed(Node.Parsedness parsed) {
      this.parsed = parsed;
      return this;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.comment != null && node == this.comment) {
         this.setComment((Comment)replacementNode);
         return true;
      } else {
         return false;
      }
   }

   public Node findRootNode() {
      Node n;
      for(n = this; n.getParentNode().isPresent(); n = (Node)n.getParentNode().get()) {
      }

      return n;
   }

   public Optional<CompilationUnit> findCompilationUnit() {
      Node rootNode = this.findRootNode();
      return rootNode instanceof CompilationUnit ? Optional.of((CompilationUnit)rootNode) : Optional.empty();
   }

   protected SymbolResolver getSymbolResolver() {
      return (SymbolResolver)this.findCompilationUnit().map((cu) -> {
         SymbolResolver symbolResolver = (SymbolResolver)cu.getData(SYMBOL_RESOLVER_KEY);
         if (symbolResolver == null) {
            throw new IllegalStateException("Symbol resolution not configured: to configure consider setting a SymbolResolver in the ParserConfiguration");
         } else {
            return symbolResolver;
         }
      }).orElseThrow(() -> {
         return new IllegalStateException("The node is not inserted in a CompilationUnit");
      });
   }

   private Iterator<Node> treeIterator(Node.TreeTraversal traversal) {
      switch(traversal) {
      case BREADTHFIRST:
         return new Node.BreadthFirstIterator(this);
      case POSTORDER:
         return new Node.PostOrderIterator(this);
      case PREORDER:
         return new Node.PreOrderIterator(this);
      case DIRECT_CHILDREN:
         return new Node.DirectChildrenIterator(this);
      case PARENTS:
         return new Node.ParentsVisitor(this);
      default:
         throw new IllegalArgumentException("Unknown traversal choice.");
      }
   }

   private Iterable<Node> treeIterable(Node.TreeTraversal traversal) {
      return () -> {
         return this.treeIterator(traversal);
      };
   }

   public Stream<Node> stream(Node.TreeTraversal traversal) {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.treeIterator(traversal), 257), false);
   }

   public Stream<Node> stream() {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.treeIterator(Node.TreeTraversal.PREORDER), 257), false);
   }

   public void walk(Node.TreeTraversal traversal, Consumer<Node> consumer) {
      Iterator var3 = this.treeIterable(traversal).iterator();

      while(var3.hasNext()) {
         Node node = (Node)var3.next();
         consumer.accept(node);
      }

   }

   public void walk(Consumer<Node> consumer) {
      this.walk(Node.TreeTraversal.PREORDER, consumer);
   }

   public <T extends Node> void walk(Class<T> nodeType, Consumer<T> consumer) {
      this.walk(Node.TreeTraversal.PREORDER, (node) -> {
         if (nodeType.isAssignableFrom(node.getClass())) {
            consumer.accept(nodeType.cast(node));
         }

      });
   }

   public <T extends Node> List<T> findAll(Class<T> nodeType) {
      List<T> found = new ArrayList();
      this.walk(nodeType, found::add);
      return found;
   }

   public <T extends Node> List<T> findAll(Class<T> nodeType, Predicate<T> predicate) {
      List<T> found = new ArrayList();
      this.walk(nodeType, (n) -> {
         if (predicate.test(n)) {
            found.add(n);
         }

      });
      return found;
   }

   public <T> Optional<T> findFirst(Node.TreeTraversal traversal, Function<Node, Optional<T>> consumer) {
      Iterator var3 = this.treeIterable(traversal).iterator();

      Optional result;
      do {
         if (!var3.hasNext()) {
            return Optional.empty();
         }

         Node node = (Node)var3.next();
         result = (Optional)consumer.apply(node);
      } while(!result.isPresent());

      return result;
   }

   public <N extends Node> Optional<N> findFirst(Class<N> nodeType) {
      return this.findFirst(Node.TreeTraversal.PREORDER, (node) -> {
         return nodeType.isAssignableFrom(node.getClass()) ? Optional.of(nodeType.cast(node)) : Optional.empty();
      });
   }

   public <N extends Node> Optional<N> findFirst(Class<N> nodeType, Predicate<N> predicate) {
      return this.findFirst(Node.TreeTraversal.PREORDER, (node) -> {
         if (nodeType.isAssignableFrom(node.getClass())) {
            N castNode = (Node)nodeType.cast(node);
            if (predicate.test(castNode)) {
               return Optional.of(castNode);
            }
         }

         return Optional.empty();
      });
   }

   public static class PostOrderIterator implements Iterator<Node> {
      private final Stack<List<Node>> nodesStack = new Stack();
      private final Stack<Integer> cursorStack = new Stack();
      private final Node root;
      private boolean hasNext = true;

      public PostOrderIterator(Node root) {
         this.root = root;
         this.fillStackToLeaf(root);
      }

      private void fillStackToLeaf(Node node) {
         while(true) {
            List<Node> childNodes = new ArrayList(node.getChildNodes());
            if (childNodes.isEmpty()) {
               return;
            }

            this.nodesStack.push(childNodes);
            this.cursorStack.push(0);
            node = (Node)childNodes.get(0);
         }
      }

      public boolean hasNext() {
         return this.hasNext;
      }

      public Node next() {
         List<Node> nodes = (List)this.nodesStack.peek();
         int cursor = (Integer)this.cursorStack.peek();
         boolean levelHasNext = cursor < nodes.size();
         if (levelHasNext) {
            Node node = (Node)nodes.get(cursor);
            this.fillStackToLeaf(node);
            return this.nextFromLevel();
         } else {
            this.nodesStack.pop();
            this.cursorStack.pop();
            this.hasNext = !this.nodesStack.empty();
            return this.hasNext ? this.nextFromLevel() : this.root;
         }
      }

      private Node nextFromLevel() {
         List<Node> nodes = (List)this.nodesStack.peek();
         int cursor = (Integer)this.cursorStack.pop();
         this.cursorStack.push(cursor + 1);
         return (Node)nodes.get(cursor);
      }
   }

   public static class PreOrderIterator implements Iterator<Node> {
      private final Stack<Node> stack = new Stack();

      public PreOrderIterator(Node node) {
         this.stack.add(node);
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public Node next() {
         Node next = (Node)this.stack.pop();
         List<Node> children = next.getChildNodes();

         for(int i = children.size() - 1; i >= 0; --i) {
            this.stack.add(children.get(i));
         }

         return next;
      }
   }

   public static class ParentsVisitor implements Iterator<Node> {
      private Node node;

      public ParentsVisitor(Node node) {
         this.node = node;
      }

      public boolean hasNext() {
         return this.node.getParentNode().isPresent();
      }

      public Node next() {
         this.node = (Node)this.node.getParentNode().orElse((Object)null);
         return this.node;
      }
   }

   public static class DirectChildrenIterator implements Iterator<Node> {
      private final Iterator<Node> childrenIterator;

      public DirectChildrenIterator(Node node) {
         this.childrenIterator = (new ArrayList(node.getChildNodes())).iterator();
      }

      public boolean hasNext() {
         return this.childrenIterator.hasNext();
      }

      public Node next() {
         return (Node)this.childrenIterator.next();
      }
   }

   public static class BreadthFirstIterator implements Iterator<Node> {
      private final Queue<Node> queue = new LinkedList();

      public BreadthFirstIterator(Node node) {
         this.queue.add(node);
      }

      public boolean hasNext() {
         return !this.queue.isEmpty();
      }

      public Node next() {
         Node next = (Node)this.queue.remove();
         this.queue.addAll(next.getChildNodes());
         return next;
      }
   }

   public static enum TreeTraversal {
      PREORDER,
      BREADTHFIRST,
      POSTORDER,
      PARENTS,
      DIRECT_CHILDREN;
   }

   public static enum Parsedness {
      PARSED,
      UNPARSABLE;
   }

   public static enum ObserverRegistrationMode {
      JUST_THIS_NODE,
      THIS_NODE_AND_EXISTING_DESCENDANTS,
      SELF_PROPAGATING;
   }
}
