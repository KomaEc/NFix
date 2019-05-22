package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalScope;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.MethodCallExprMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class MethodCallExpr extends Expression implements NodeWithTypeArguments<MethodCallExpr>, NodeWithArguments<MethodCallExpr>, NodeWithSimpleName<MethodCallExpr>, NodeWithOptionalScope<MethodCallExpr>, Resolvable<ResolvedMethodDeclaration> {
   @OptionalProperty
   private Expression scope;
   @OptionalProperty
   private NodeList<Type> typeArguments;
   private SimpleName name;
   private NodeList<Expression> arguments;

   public MethodCallExpr() {
      this((TokenRange)null, (Expression)null, (NodeList)null, new SimpleName(), new NodeList());
   }

   public MethodCallExpr(String name, Expression... arguments) {
      this((TokenRange)null, (Expression)null, (NodeList)null, new SimpleName(name), new NodeList(arguments));
   }

   public MethodCallExpr(final Expression scope, final String name) {
      this((TokenRange)null, scope, (NodeList)null, new SimpleName(name), new NodeList());
   }

   public MethodCallExpr(final Expression scope, final SimpleName name) {
      this((TokenRange)null, scope, (NodeList)null, name, new NodeList());
   }

   public MethodCallExpr(final Expression scope, final String name, final NodeList<Expression> arguments) {
      this((TokenRange)null, scope, (NodeList)null, new SimpleName(name), arguments);
   }

   public MethodCallExpr(final Expression scope, final NodeList<Type> typeArguments, final String name, final NodeList<Expression> arguments) {
      this((TokenRange)null, scope, typeArguments, new SimpleName(name), arguments);
   }

   public MethodCallExpr(final Expression scope, final SimpleName name, final NodeList<Expression> arguments) {
      this((TokenRange)null, scope, (NodeList)null, name, arguments);
   }

   @AllFieldsConstructor
   public MethodCallExpr(final Expression scope, final NodeList<Type> typeArguments, final SimpleName name, final NodeList<Expression> arguments) {
      this((TokenRange)null, scope, typeArguments, name, arguments);
   }

   public MethodCallExpr(TokenRange tokenRange, Expression scope, NodeList<Type> typeArguments, SimpleName name, NodeList<Expression> arguments) {
      super(tokenRange);
      this.setScope(scope);
      this.setTypeArguments(typeArguments);
      this.setName(name);
      this.setArguments(arguments);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<Expression> getArguments() {
      return this.arguments;
   }

   public SimpleName getName() {
      return this.name;
   }

   public Optional<Expression> getScope() {
      return Optional.ofNullable(this.scope);
   }

   public MethodCallExpr setArguments(final NodeList<Expression> arguments) {
      Utils.assertNotNull(arguments);
      if (arguments == this.arguments) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ARGUMENTS, this.arguments, arguments);
         if (this.arguments != null) {
            this.arguments.setParentNode((Node)null);
         }

         this.arguments = arguments;
         this.setAsParentNodeOf(arguments);
         return this;
      }
   }

   public MethodCallExpr setName(final SimpleName name) {
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

   public MethodCallExpr setScope(final Expression scope) {
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

   public MethodCallExpr setTypeArguments(final NodeList<Type> typeArguments) {
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

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == node) {
               this.arguments.remove(i);
               return true;
            }
         }

         if (this.scope != null && node == this.scope) {
            this.removeScope();
            return true;
         } else {
            if (this.typeArguments != null) {
               for(i = 0; i < this.typeArguments.size(); ++i) {
                  if (this.typeArguments.get(i) == node) {
                     this.typeArguments.remove(i);
                     return true;
                  }
               }
            }

            return super.remove(node);
         }
      }
   }

   public MethodCallExpr removeScope() {
      return this.setScope((Expression)null);
   }

   public MethodCallExpr clone() {
      return (MethodCallExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public MethodCallExprMetaModel getMetaModel() {
      return JavaParserMetaModel.methodCallExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == node) {
               this.arguments.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         if (node == this.name) {
            this.setName((SimpleName)replacementNode);
            return true;
         } else if (this.scope != null && node == this.scope) {
            this.setScope((Expression)replacementNode);
            return true;
         } else {
            if (this.typeArguments != null) {
               for(i = 0; i < this.typeArguments.size(); ++i) {
                  if (this.typeArguments.get(i) == node) {
                     this.typeArguments.set(i, (Node)((Type)replacementNode));
                     return true;
                  }
               }
            }

            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isMethodCallExpr() {
      return true;
   }

   public MethodCallExpr asMethodCallExpr() {
      return this;
   }

   public void ifMethodCallExpr(Consumer<MethodCallExpr> action) {
      action.accept(this);
   }

   public ResolvedMethodDeclaration resolve() {
      return (ResolvedMethodDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedMethodDeclaration.class);
   }

   /** @deprecated */
   @Deprecated
   public ResolvedMethodDeclaration resolveInvokedMethod() {
      return this.resolve();
   }

   public Optional<MethodCallExpr> toMethodCallExpr() {
      return Optional.of(this);
   }
}
