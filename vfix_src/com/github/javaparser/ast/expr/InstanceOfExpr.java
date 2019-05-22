package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.InstanceOfExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class InstanceOfExpr extends Expression implements NodeWithType<InstanceOfExpr, ReferenceType>, NodeWithExpression<InstanceOfExpr> {
   private Expression expression;
   private ReferenceType type;

   public InstanceOfExpr() {
      this((TokenRange)null, new NameExpr(), new ClassOrInterfaceType());
   }

   @AllFieldsConstructor
   public InstanceOfExpr(final Expression expression, final ReferenceType type) {
      this((TokenRange)null, expression, type);
   }

   public InstanceOfExpr(TokenRange tokenRange, Expression expression, ReferenceType type) {
      super(tokenRange);
      this.setExpression(expression);
      this.setType(type);
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

   public ReferenceType getType() {
      return this.type;
   }

   public InstanceOfExpr setExpression(final Expression expression) {
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

   public InstanceOfExpr setType(final ReferenceType type) {
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

   public InstanceOfExpr clone() {
      return (InstanceOfExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public InstanceOfExprMetaModel getMetaModel() {
      return JavaParserMetaModel.instanceOfExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.expression) {
         this.setExpression((Expression)replacementNode);
         return true;
      } else if (node == this.type) {
         this.setType((ReferenceType)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isInstanceOfExpr() {
      return true;
   }

   public InstanceOfExpr asInstanceOfExpr() {
      return this;
   }

   public void ifInstanceOfExpr(Consumer<InstanceOfExpr> action) {
      action.accept(this);
   }

   public Optional<InstanceOfExpr> toInstanceOfExpr() {
      return Optional.of(this);
   }
}
