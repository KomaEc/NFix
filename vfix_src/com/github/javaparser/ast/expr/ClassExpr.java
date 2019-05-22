package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ClassExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ClassExpr extends Expression implements NodeWithType<ClassExpr, Type> {
   private Type type;

   public ClassExpr() {
      this((TokenRange)null, new ClassOrInterfaceType());
   }

   @AllFieldsConstructor
   public ClassExpr(Type type) {
      this((TokenRange)null, type);
   }

   public ClassExpr(TokenRange tokenRange, Type type) {
      super(tokenRange);
      this.setType(type);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Type getType() {
      return this.type;
   }

   public ClassExpr setType(final Type type) {
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

   public ClassExpr clone() {
      return (ClassExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ClassExprMetaModel getMetaModel() {
      return JavaParserMetaModel.classExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.type) {
         this.setType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isClassExpr() {
      return true;
   }

   public ClassExpr asClassExpr() {
      return this;
   }

   public void ifClassExpr(Consumer<ClassExpr> action) {
      action.accept(this);
   }

   public Optional<ClassExpr> toClassExpr() {
      return Optional.of(this);
   }
}
