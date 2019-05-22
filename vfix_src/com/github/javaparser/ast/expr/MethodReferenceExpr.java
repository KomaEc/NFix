package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.MethodReferenceExprMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class MethodReferenceExpr extends Expression implements NodeWithTypeArguments<MethodReferenceExpr>, NodeWithIdentifier<MethodReferenceExpr> {
   private Expression scope;
   @OptionalProperty
   private NodeList<Type> typeArguments;
   @NonEmptyProperty
   private String identifier;

   public MethodReferenceExpr() {
      this((TokenRange)null, new ClassExpr(), (NodeList)null, "empty");
   }

   @AllFieldsConstructor
   public MethodReferenceExpr(Expression scope, NodeList<Type> typeArguments, String identifier) {
      this((TokenRange)null, scope, typeArguments, identifier);
   }

   public MethodReferenceExpr(TokenRange tokenRange, Expression scope, NodeList<Type> typeArguments, String identifier) {
      super(tokenRange);
      this.setScope(scope);
      this.setTypeArguments(typeArguments);
      this.setIdentifier(identifier);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getScope() {
      return this.scope;
   }

   public MethodReferenceExpr setScope(final Expression scope) {
      Utils.assertNotNull(scope);
      if (scope == this.scope) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.SCOPE, this.scope, scope);
         if (this.scope != null) {
            this.scope.setParentNode((Node)null);
         }

         this.scope = scope;
         this.setAsParentNodeOf(scope);
         return this;
      }
   }

   public Optional<NodeList<Type>> getTypeArguments() {
      return Optional.ofNullable(this.typeArguments);
   }

   public MethodReferenceExpr setTypeArguments(final NodeList<Type> typeArguments) {
      if (typeArguments == this.typeArguments) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE_ARGUMENTS, this.typeArguments, typeArguments);
         if (this.typeArguments != null) {
            this.typeArguments.setParentNode((Node)null);
         }

         this.typeArguments = typeArguments;
         this.setAsParentNodeOf(typeArguments);
         return this;
      }
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public MethodReferenceExpr setIdentifier(final String identifier) {
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
      if (node == null) {
         return false;
      } else {
         if (this.typeArguments != null) {
            for(int i = 0; i < this.typeArguments.size(); ++i) {
               if (this.typeArguments.get(i) == node) {
                  this.typeArguments.remove(i);
                  return true;
               }
            }
         }

         return super.remove(node);
      }
   }

   public MethodReferenceExpr clone() {
      return (MethodReferenceExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public MethodReferenceExprMetaModel getMetaModel() {
      return JavaParserMetaModel.methodReferenceExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.scope) {
         this.setScope((Expression)replacementNode);
         return true;
      } else {
         if (this.typeArguments != null) {
            for(int i = 0; i < this.typeArguments.size(); ++i) {
               if (this.typeArguments.get(i) == node) {
                  this.typeArguments.set(i, (Node)((Type)replacementNode));
                  return true;
               }
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isMethodReferenceExpr() {
      return true;
   }

   public MethodReferenceExpr asMethodReferenceExpr() {
      return this;
   }

   public void ifMethodReferenceExpr(Consumer<MethodReferenceExpr> action) {
      action.accept(this);
   }

   public Optional<MethodReferenceExpr> toMethodReferenceExpr() {
      return Optional.of(this);
   }
}
