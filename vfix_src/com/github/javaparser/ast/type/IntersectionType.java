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
import com.github.javaparser.metamodel.IntersectionTypeMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.resolution.types.ResolvedIntersectionType;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class IntersectionType extends Type implements NodeWithAnnotations<IntersectionType> {
   @NonEmptyProperty
   private NodeList<ReferenceType> elements;

   @AllFieldsConstructor
   public IntersectionType(NodeList<ReferenceType> elements) {
      this((TokenRange)null, elements);
   }

   public IntersectionType(TokenRange tokenRange, NodeList<ReferenceType> elements) {
      super(tokenRange);
      this.setElements(elements);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<ReferenceType> getElements() {
      return this.elements;
   }

   public IntersectionType setElements(final NodeList<ReferenceType> elements) {
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

   public IntersectionType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (IntersectionType)super.setAnnotations(annotations);
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
      return (String)this.elements.stream().map(Type::asString).collect(Collectors.joining("&"));
   }

   public IntersectionType clone() {
      return (IntersectionType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public IntersectionTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.intersectionTypeMetaModel;
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

   public boolean isIntersectionType() {
      return true;
   }

   public IntersectionType asIntersectionType() {
      return this;
   }

   public void ifIntersectionType(Consumer<IntersectionType> action) {
      action.accept(this);
   }

   public ResolvedIntersectionType resolve() {
      return (ResolvedIntersectionType)this.getSymbolResolver().toResolvedType(this, ResolvedIntersectionType.class);
   }

   public Optional<IntersectionType> toIntersectionType() {
      return Optional.of(this);
   }
}
