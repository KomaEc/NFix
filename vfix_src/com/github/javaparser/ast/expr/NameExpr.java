package com.github.javaparser.ast.expr;

import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NameExprMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class NameExpr extends Expression implements NodeWithSimpleName<NameExpr>, Resolvable<ResolvedValueDeclaration> {
   private SimpleName name;

   public NameExpr() {
      this((TokenRange)null, new SimpleName());
   }

   public NameExpr(final String name) {
      this((TokenRange)null, new SimpleName(name));
   }

   @AllFieldsConstructor
   public NameExpr(final SimpleName name) {
      this((TokenRange)name.getTokenRange().orElse((Object)null), name);
      this.setRange((Range)name.getRange().orElse((Object)null));
   }

   public NameExpr(TokenRange tokenRange, SimpleName name) {
      super(tokenRange);
      this.setName(name);
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

   public NameExpr setName(final SimpleName name) {
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

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public NameExpr clone() {
      return (NameExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public NameExprMetaModel getMetaModel() {
      return JavaParserMetaModel.nameExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isNameExpr() {
      return true;
   }

   public NameExpr asNameExpr() {
      return this;
   }

   public void ifNameExpr(Consumer<NameExpr> action) {
      action.accept(this);
   }

   public ResolvedValueDeclaration resolve() {
      return (ResolvedValueDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedValueDeclaration.class);
   }

   public Optional<NameExpr> toNameExpr() {
      return Optional.of(this);
   }
}
