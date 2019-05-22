package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithScope;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.FieldAccessExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class FieldAccessExpr extends Expression implements NodeWithSimpleName<FieldAccessExpr>, NodeWithTypeArguments<FieldAccessExpr>, NodeWithScope<FieldAccessExpr>, Resolvable<ResolvedValueDeclaration> {
   private Expression scope;
   @OptionalProperty
   private NodeList<Type> typeArguments;
   private SimpleName name;

   public FieldAccessExpr() {
      this((TokenRange)null, new ThisExpr(), (NodeList)null, new SimpleName());
   }

   public FieldAccessExpr(final Expression scope, final String name) {
      this((TokenRange)null, scope, (NodeList)null, new SimpleName(name));
   }

   @AllFieldsConstructor
   public FieldAccessExpr(final Expression scope, final NodeList<Type> typeArguments, final SimpleName name) {
      this((TokenRange)null, scope, typeArguments, name);
   }

   public FieldAccessExpr(TokenRange tokenRange, Expression scope, NodeList<Type> typeArguments, SimpleName name) {
      super(tokenRange);
      this.setScope(scope);
      this.setTypeArguments(typeArguments);
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

   public FieldAccessExpr setName(final SimpleName name) {
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

   /** @deprecated */
   @Deprecated
   public SimpleName getField() {
      return this.name;
   }

   public Expression getScope() {
      return this.scope;
   }

   /** @deprecated */
   @Deprecated
   public FieldAccessExpr setField(final String field) {
      this.setName(new SimpleName(field));
      return this;
   }

   /** @deprecated */
   @Deprecated
   public FieldAccessExpr setFieldExpr(SimpleName inner) {
      return this.setName(inner);
   }

   public FieldAccessExpr setScope(final Expression scope) {
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

   public FieldAccessExpr setTypeArguments(final NodeList<Type> typeArguments) {
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

   public FieldAccessExpr clone() {
      return (FieldAccessExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public FieldAccessExprMetaModel getMetaModel() {
      return JavaParserMetaModel.fieldAccessExprMetaModel;
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

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
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

   public boolean isFieldAccessExpr() {
      return true;
   }

   public FieldAccessExpr asFieldAccessExpr() {
      return this;
   }

   public void ifFieldAccessExpr(Consumer<FieldAccessExpr> action) {
      action.accept(this);
   }

   public ResolvedValueDeclaration resolve() {
      return (ResolvedValueDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedValueDeclaration.class);
   }

   public Optional<FieldAccessExpr> toFieldAccessExpr() {
      return Optional.of(this);
   }

   public boolean isInternal() {
      return this.getParentNode().isPresent() && this.getParentNode().get() instanceof FieldAccessExpr;
   }

   public boolean isTopLevel() {
      return !this.isInternal();
   }
}
