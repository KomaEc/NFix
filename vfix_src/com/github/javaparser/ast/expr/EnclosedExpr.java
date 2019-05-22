package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.EnclosedExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class EnclosedExpr extends Expression {
   private Expression inner;

   public EnclosedExpr() {
      this((TokenRange)null, new StringLiteralExpr());
   }

   @AllFieldsConstructor
   public EnclosedExpr(final Expression inner) {
      this((TokenRange)null, inner);
   }

   public EnclosedExpr(TokenRange tokenRange, Expression inner) {
      super(tokenRange);
      this.setInner(inner);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getInner() {
      return this.inner;
   }

   public EnclosedExpr setInner(final Expression inner) {
      Utils.assertNotNull(inner);
      if (inner == this.inner) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INNER, this.inner, inner);
         if (this.inner != null) {
            this.inner.setParentNode((Node)null);
         }

         this.inner = inner;
         this.setAsParentNodeOf(inner);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public EnclosedExpr removeInner() {
      return this.setInner((Expression)null);
   }

   public EnclosedExpr clone() {
      return (EnclosedExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public EnclosedExprMetaModel getMetaModel() {
      return JavaParserMetaModel.enclosedExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.inner) {
         this.setInner((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isEnclosedExpr() {
      return true;
   }

   public EnclosedExpr asEnclosedExpr() {
      return this;
   }

   public void ifEnclosedExpr(Consumer<EnclosedExpr> action) {
      action.accept(this);
   }

   public Optional<EnclosedExpr> toEnclosedExpr() {
      return Optional.of(this);
   }
}
