package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.AnnotationExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AnnotationExpr extends Expression implements NodeWithName<AnnotationExpr>, Resolvable<ResolvedAnnotationDeclaration> {
   protected Name name;

   public AnnotationExpr() {
      this((TokenRange)null, new Name());
   }

   @AllFieldsConstructor
   public AnnotationExpr(Name name) {
      this((TokenRange)null, name);
   }

   public AnnotationExpr(TokenRange tokenRange, Name name) {
      super(tokenRange);
      this.setName(name);
      this.customInitialization();
   }

   public Name getName() {
      return this.name;
   }

   public AnnotationExpr setName(final Name name) {
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

   public AnnotationExpr clone() {
      return (AnnotationExpr)this.accept(new CloneVisitor(), (Object)null);
   }

   public AnnotationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.annotationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((Name)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isAnnotationExpr() {
      return true;
   }

   public AnnotationExpr asAnnotationExpr() {
      return this;
   }

   public void ifAnnotationExpr(Consumer<AnnotationExpr> action) {
      action.accept(this);
   }

   public ResolvedAnnotationDeclaration resolve() {
      return (ResolvedAnnotationDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedAnnotationDeclaration.class);
   }

   public Optional<AnnotationExpr> toAnnotationExpr() {
      return Optional.of(this);
   }
}
