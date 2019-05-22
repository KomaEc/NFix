package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.SimpleNameMetaModel;
import com.github.javaparser.utils.Utils;

public final class SimpleName extends Node implements NodeWithIdentifier<SimpleName> {
   @NonEmptyProperty
   private String identifier;

   public SimpleName() {
      this((TokenRange)null, "empty");
   }

   @AllFieldsConstructor
   public SimpleName(final String identifier) {
      this((TokenRange)null, identifier);
   }

   public SimpleName(TokenRange tokenRange, String identifier) {
      super(tokenRange);
      this.setIdentifier(identifier);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public SimpleName setIdentifier(final String identifier) {
      Utils.assertNonEmpty(identifier);
      if (identifier == this.identifier) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.IDENTIFIER, this.identifier, identifier);
         this.identifier = identifier;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public String asString() {
      return this.identifier;
   }

   public SimpleName clone() {
      return (SimpleName)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SimpleNameMetaModel getMetaModel() {
      return JavaParserMetaModel.simpleNameMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }
}
