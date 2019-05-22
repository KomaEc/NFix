package com.github.javaparser.ast;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ArrayCreationLevelMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;

public final class ArrayCreationLevel extends Node implements NodeWithAnnotations<ArrayCreationLevel> {
   @OptionalProperty
   private Expression dimension;
   private NodeList<AnnotationExpr> annotations;

   public ArrayCreationLevel() {
      this((TokenRange)null, (Expression)null, new NodeList());
   }

   public ArrayCreationLevel(int dimension) {
      this((TokenRange)null, new IntegerLiteralExpr("" + dimension), new NodeList());
   }

   public ArrayCreationLevel(Expression dimension) {
      this((TokenRange)null, dimension, new NodeList());
   }

   @AllFieldsConstructor
   public ArrayCreationLevel(Expression dimension, NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, dimension, annotations);
   }

   public ArrayCreationLevel(TokenRange tokenRange, Expression dimension, NodeList<AnnotationExpr> annotations) {
      super(tokenRange);
      this.annotations = new NodeList();
      this.setDimension(dimension);
      this.setAnnotations(annotations);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public ArrayCreationLevel setDimension(final Expression dimension) {
      if (dimension == this.dimension) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.DIMENSION, this.dimension, dimension);
         if (this.dimension != null) {
            this.dimension.setParentNode((Node)null);
         }

         this.dimension = dimension;
         this.setAsParentNodeOf(dimension);
         return this;
      }
   }

   public Optional<Expression> getDimension() {
      return Optional.ofNullable(this.dimension);
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public ArrayCreationLevel setAnnotations(final NodeList<AnnotationExpr> annotations) {
      Utils.assertNotNull(annotations);
      if (annotations == this.annotations) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ANNOTATIONS, this.annotations, annotations);
         if (this.annotations != null) {
            this.annotations.setParentNode((Node)null);
         }

         this.annotations = annotations;
         this.setAsParentNodeOf(annotations);
         return this;
      }
   }

   public ArrayCreationLevel removeDimension() {
      return this.setDimension((Expression)null);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.remove(i);
               return true;
            }
         }

         if (this.dimension != null && node == this.dimension) {
            this.removeDimension();
            return true;
         } else {
            return super.remove(node);
         }
      }
   }

   public ArrayCreationLevel clone() {
      return (ArrayCreationLevel)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ArrayCreationLevelMetaModel getMetaModel() {
      return JavaParserMetaModel.arrayCreationLevelMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.set(i, (Node)((AnnotationExpr)replacementNode));
               return true;
            }
         }

         if (this.dimension != null && node == this.dimension) {
            this.setDimension((Expression)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }
}
