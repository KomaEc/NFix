package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.ThisExprMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import java.util.Optional;
import java.util.function.Consumer;

public final class ThisExpr extends Expression implements Resolvable<ResolvedTypeDeclaration> {
   @OptionalProperty
   private Expression classExpr;

   public ThisExpr() {
      this((TokenRange)null, (Expression)null);
   }

   @AllFieldsConstructor
   public ThisExpr(final Expression classExpr) {
      this((TokenRange)null, classExpr);
   }

   public ThisExpr(TokenRange tokenRange, Expression classExpr) {
      super(tokenRange);
      this.setClassExpr(classExpr);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getClassExpr() {
      return Optional.ofNullable(this.classExpr);
   }

   public ThisExpr setClassExpr(final Expression classExpr) {
      if (classExpr == this.classExpr) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CLASS_EXPR, this.classExpr, classExpr);
         if (this.classExpr != null) {
            this.classExpr.setParentNode((Node)null);
         }

         this.classExpr = classExpr;
         this.setAsParentNodeOf(classExpr);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.classExpr != null && node == this.classExpr) {
         this.removeClassExpr();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public ThisExpr removeClassExpr() {
      return this.setClassExpr((Expression)null);
   }

   public ThisExpr clone() {
      return (ThisExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ThisExprMetaModel getMetaModel() {
      return JavaParserMetaModel.thisExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.classExpr != null && node == this.classExpr) {
         this.setClassExpr((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isThisExpr() {
      return true;
   }

   public ThisExpr asThisExpr() {
      return this;
   }

   public void ifThisExpr(Consumer<ThisExpr> action) {
      action.accept(this);
   }

   public ResolvedTypeDeclaration resolve() {
      return (ResolvedTypeDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedTypeDeclaration.class);
   }

   public Optional<ThisExpr> toThisExpr() {
      return Optional.of(this);
   }
}
