package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ExplicitConstructorInvocationStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ExplicitConstructorInvocationStmt extends Statement implements NodeWithTypeArguments<ExplicitConstructorInvocationStmt>, NodeWithArguments<ExplicitConstructorInvocationStmt>, Resolvable<ResolvedConstructorDeclaration> {
   @OptionalProperty
   private NodeList<Type> typeArguments;
   private boolean isThis;
   @OptionalProperty
   private Expression expression;
   private NodeList<Expression> arguments;

   public ExplicitConstructorInvocationStmt() {
      this((TokenRange)null, (NodeList)null, true, (Expression)null, new NodeList());
   }

   public ExplicitConstructorInvocationStmt(final boolean isThis, final Expression expression, final NodeList<Expression> arguments) {
      this((TokenRange)null, (NodeList)null, isThis, expression, arguments);
   }

   @AllFieldsConstructor
   public ExplicitConstructorInvocationStmt(final NodeList<Type> typeArguments, final boolean isThis, final Expression expression, final NodeList<Expression> arguments) {
      this((TokenRange)null, typeArguments, isThis, expression, arguments);
   }

   public ExplicitConstructorInvocationStmt(TokenRange tokenRange, NodeList<Type> typeArguments, boolean isThis, Expression expression, NodeList<Expression> arguments) {
      super(tokenRange);
      this.setTypeArguments(typeArguments);
      this.setThis(isThis);
      this.setExpression(expression);
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

   public Optional<Expression> getExpression() {
      return Optional.ofNullable(this.expression);
   }

   public boolean isThis() {
      return this.isThis;
   }

   public ExplicitConstructorInvocationStmt setArguments(final NodeList<Expression> arguments) {
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

   public ExplicitConstructorInvocationStmt setExpression(final Expression expression) {
      if (expression == this.expression) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.EXPRESSION, this.expression, expression);
         if (this.expression != null) {
            this.expression.setParentNode((Node)null);
         }

         this.expression = expression;
         this.setAsParentNodeOf(expression);
         return this;
      }
   }

   public ExplicitConstructorInvocationStmt setThis(final boolean isThis) {
      if (isThis == this.isThis) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.THIS, this.isThis, isThis);
         this.isThis = isThis;
         return this;
      }
   }

   public Optional<NodeList<Type>> getTypeArguments() {
      return Optional.ofNullable(this.typeArguments);
   }

   public ExplicitConstructorInvocationStmt setTypeArguments(final NodeList<Type> typeArguments) {
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

         if (this.expression != null && node == this.expression) {
            this.removeExpression();
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

   public ExplicitConstructorInvocationStmt removeExpression() {
      return this.setExpression((Expression)null);
   }

   public ExplicitConstructorInvocationStmt clone() {
      return (ExplicitConstructorInvocationStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ExplicitConstructorInvocationStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.explicitConstructorInvocationStmtMetaModel;
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

         if (this.expression != null && node == this.expression) {
            this.setExpression((Expression)replacementNode);
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

   public boolean isExplicitConstructorInvocationStmt() {
      return true;
   }

   public ExplicitConstructorInvocationStmt asExplicitConstructorInvocationStmt() {
      return this;
   }

   public void ifExplicitConstructorInvocationStmt(Consumer<ExplicitConstructorInvocationStmt> action) {
      action.accept(this);
   }

   public ResolvedConstructorDeclaration resolve() {
      return (ResolvedConstructorDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedConstructorDeclaration.class);
   }

   /** @deprecated */
   @Deprecated
   public ResolvedConstructorDeclaration resolveInvokedConstructor() {
      return this.resolve();
   }

   public Optional<ExplicitConstructorInvocationStmt> toExplicitConstructorInvocationStmt() {
      return Optional.of(this);
   }
}
