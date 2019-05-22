package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.CastExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class CastExpr extends Expression implements NodeWithType<CastExpr, Type>, NodeWithExpression<CastExpr> {
   private Type type;
   private Expression expression;

   public CastExpr() {
      this((TokenRange)null, new ClassOrInterfaceType(), new NameExpr());
   }

   @AllFieldsConstructor
   public CastExpr(Type type, Expression expression) {
      this((TokenRange)null, type, expression);
   }

   public CastExpr(TokenRange tokenRange, Type type, Expression expression) {
      super(tokenRange);
      this.setType(type);
      this.setExpression(expression);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getExpression() {
      return this.expression;
   }

   public Type getType() {
      return this.type;
   }

   public CastExpr setExpression(final Expression expression) {
      Utils.assertNotNull(expression);
      if (expression == this.expression) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.EXPRESSION, this.expression, expression);
         if (this.expression != null) {
            this.expression.setParentNode((Node)null);
         }

         this.expression = expression;
         this.setAsParentNodeOf(expression);
         return this;
      }
   }

   public CastExpr setType(final Type type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         if (this.type != null) {
            this.type.setParentNode((Node)null);
         }

         this.type = type;
         this.setAsParentNodeOf(type);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public CastExpr clone() {
      return (CastExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public CastExprMetaModel getMetaModel() {
      return JavaParserMetaModel.castExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.expression) {
         this.setExpression((Expression)replacementNode);
         return true;
      } else if (node == this.type) {
         this.setType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isCastExpr() {
      return true;
   }

   public CastExpr asCastExpr() {
      return this;
   }

   public void ifCastExpr(Consumer<CastExpr> action) {
      action.accept(this);
   }

   public Optional<CastExpr> toCastExpr() {
      return Optional.of(this);
   }
}
