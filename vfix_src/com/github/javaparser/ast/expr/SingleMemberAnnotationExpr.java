package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.SingleMemberAnnotationExprMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class SingleMemberAnnotationExpr extends AnnotationExpr {
   private Expression memberValue;

   public SingleMemberAnnotationExpr() {
      this((TokenRange)null, new Name(), new StringLiteralExpr());
   }

   @AllFieldsConstructor
   public SingleMemberAnnotationExpr(final Name name, final Expression memberValue) {
      this((TokenRange)null, name, memberValue);
   }

   public SingleMemberAnnotationExpr(TokenRange tokenRange, Name name, Expression memberValue) {
      super(tokenRange, name);
      this.setMemberValue(memberValue);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getMemberValue() {
      return this.memberValue;
   }

   public SingleMemberAnnotationExpr setMemberValue(final Expression memberValue) {
      Utils.assertNotNull(memberValue);
      if (memberValue == this.memberValue) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MEMBER_VALUE, this.memberValue, memberValue);
         if (this.memberValue != null) {
            this.memberValue.setParentNode((Node)null);
         }

         this.memberValue = memberValue;
         this.setAsParentNodeOf(memberValue);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public SingleMemberAnnotationExpr clone() {
      return (SingleMemberAnnotationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SingleMemberAnnotationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.singleMemberAnnotationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.memberValue) {
         this.setMemberValue((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isSingleMemberAnnotationExpr() {
      return true;
   }

   public SingleMemberAnnotationExpr asSingleMemberAnnotationExpr() {
      return this;
   }

   public void ifSingleMemberAnnotationExpr(Consumer<SingleMemberAnnotationExpr> action) {
      action.accept(this);
   }

   public Optional<SingleMemberAnnotationExpr> toSingleMemberAnnotationExpr() {
      return Optional.of(this);
   }
}
