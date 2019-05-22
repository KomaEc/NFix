package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.UnionTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedUnionType;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class UnionType extends Type implements NodeWithAnnotations<UnionType> {
   @NonEmptyProperty
   private NodeList<ReferenceType> elements;

   public UnionType() {
      this((TokenRange)null, new NodeList());
   }

   public UnionType(TokenRange tokenRange, NodeList<ReferenceType> elements) {
      super(tokenRange);
      this.setElements(elements);
      this.customInitialization();
   }

   @AllFieldsConstructor
   public UnionType(NodeList<ReferenceType> elements) {
      this((TokenRange)null, elements);
   }

   public NodeList<ReferenceType> getElements() {
      return this.elements;
   }

   public UnionType setElements(final NodeList<ReferenceType> elements) {
      Utils.assertNotNull(elements);
      if (elements == this.elements) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ELEMENTS, this.elements, elements);
         if (this.elements != null) {
            this.elements.setParentNode((Node)null);
         }

         this.elements = elements;
         this.setAsParentNodeOf(elements);
         return this;
      }
   }

   public UnionType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (UnionType)super.setAnnotations(annotations);
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.elements.size(); ++i) {
            if (this.elements.get(i) == node) {
               this.elements.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public String asString() {
      return (String)this.elements.stream().map(Type::asString).collect(Collectors.joining("|"));
   }

   public UnionType clone() {
      return (UnionType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public UnionTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.unionTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.elements.size(); ++i) {
            if (this.elements.get(i) == node) {
               this.elements.set(i, (Node)((ReferenceType)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isUnionType() {
      return true;
   }

   public UnionType asUnionType() {
      return this;
   }

   public void ifUnionType(Consumer<UnionType> action) {
      action.accept(this);
   }

   public ResolvedUnionType resolve() {
      return (ResolvedUnionType)this.getSymbolResolver().toResolvedType(this, ResolvedUnionType.class);
   }

   public Optional<UnionType> toUnionType() {
      return Optional.of(this);
   }
}
