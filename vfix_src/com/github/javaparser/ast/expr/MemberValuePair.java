package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.MemberValuePairMetaModel;
import com.github.javaparser.utils.Utils;

public final class MemberValuePair extends Node implements NodeWithSimpleName<MemberValuePair> {
   private SimpleName name;
   private Expression value;

   public MemberValuePair() {
      this((TokenRange)null, new SimpleName(), new StringLiteralExpr());
   }

   public MemberValuePair(final String name, final Expression value) {
      this((TokenRange)null, new SimpleName(name), value);
   }

   @AllFieldsConstructor
   public MemberValuePair(final SimpleName name, final Expression value) {
      this((TokenRange)null, name, value);
   }

   public MemberValuePair(TokenRange tokenRange, SimpleName name, Expression value) {
      super(tokenRange);
      this.setName(name);
      this.setValue(value);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public SimpleName getName() {
      return this.name;
   }

   public Expression getValue() {
      return this.value;
   }

   public MemberValuePair setName(final SimpleName name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public MemberValuePair setValue(final Expression value) {
      Utils.assertNotNull(value);
      if (value == this.value) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VALUE, this.value, value);
         if (this.value != null) {
            this.value.setParentNode((Node)null);
         }

         this.value = value;
         this.setAsParentNodeOf(value);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public MemberValuePair clone() {
      return (MemberValuePair)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public MemberValuePairMetaModel getMetaModel() {
      return JavaParserMetaModel.memberValuePairMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else if (node == this.value) {
         this.setValue((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }
}
