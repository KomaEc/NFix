package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NormalAnnotationExprMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class NormalAnnotationExpr extends AnnotationExpr {
   private NodeList<MemberValuePair> pairs;

   public NormalAnnotationExpr() {
      this((TokenRange)null, new Name(), new NodeList());
   }

   @AllFieldsConstructor
   public NormalAnnotationExpr(final Name name, final NodeList<MemberValuePair> pairs) {
      this((TokenRange)null, name, pairs);
   }

   public NormalAnnotationExpr(TokenRange tokenRange, Name name, NodeList<MemberValuePair> pairs) {
      super(tokenRange, name);
      this.setPairs(pairs);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<MemberValuePair> getPairs() {
      return this.pairs;
   }

   public NormalAnnotationExpr setPairs(final NodeList<MemberValuePair> pairs) {
      Utils.assertNotNull(pairs);
      if (pairs == this.pairs) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.PAIRS, this.pairs, pairs);
         if (this.pairs != null) {
            this.pairs.setParentNode((Node)null);
         }

         this.pairs = pairs;
         this.setAsParentNodeOf(pairs);
         return this;
      }
   }

   public NormalAnnotationExpr addPair(String key, String value) {
      return this.addPair(key, (Expression)(new NameExpr(value)));
   }

   public NormalAnnotationExpr addPair(String key, Expression value) {
      MemberValuePair memberValuePair = new MemberValuePair(key, value);
      this.getPairs().add((Node)memberValuePair);
      return this;
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.pairs.size(); ++i) {
            if (this.pairs.get(i) == node) {
               this.pairs.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public NormalAnnotationExpr clone() {
      return (NormalAnnotationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public NormalAnnotationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.normalAnnotationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.pairs.size(); ++i) {
            if (this.pairs.get(i) == node) {
               this.pairs.set(i, (Node)((MemberValuePair)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isNormalAnnotationExpr() {
      return true;
   }

   public NormalAnnotationExpr asNormalAnnotationExpr() {
      return this;
   }

   public void ifNormalAnnotationExpr(Consumer<NormalAnnotationExpr> action) {
      action.accept(this);
   }

   public Optional<NormalAnnotationExpr> toNormalAnnotationExpr() {
      return Optional.of(this);
   }
}
