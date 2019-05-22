package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalScope;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ObjectCreationExprMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ObjectCreationExpr extends Expression implements NodeWithTypeArguments<ObjectCreationExpr>, NodeWithType<ObjectCreationExpr, ClassOrInterfaceType>, NodeWithArguments<ObjectCreationExpr>, NodeWithOptionalScope<ObjectCreationExpr>, Resolvable<ResolvedConstructorDeclaration> {
   @OptionalProperty
   private Expression scope;
   private ClassOrInterfaceType type;
   @OptionalProperty
   private NodeList<Type> typeArguments;
   private NodeList<Expression> arguments;
   @OptionalProperty
   private NodeList<BodyDeclaration<?>> anonymousClassBody;

   public ObjectCreationExpr() {
      this((TokenRange)null, (Expression)null, new ClassOrInterfaceType(), new NodeList(), new NodeList(), (NodeList)null);
   }

   public ObjectCreationExpr(final Expression scope, final ClassOrInterfaceType type, final NodeList<Expression> arguments) {
      this((TokenRange)null, scope, type, new NodeList(), arguments, (NodeList)null);
   }

   @AllFieldsConstructor
   public ObjectCreationExpr(final Expression scope, final ClassOrInterfaceType type, final NodeList<Type> typeArguments, final NodeList<Expression> arguments, final NodeList<BodyDeclaration<?>> anonymousClassBody) {
      this((TokenRange)null, scope, type, typeArguments, arguments, anonymousClassBody);
   }

   public ObjectCreationExpr(TokenRange tokenRange, Expression scope, ClassOrInterfaceType type, NodeList<Type> typeArguments, NodeList<Expression> arguments, NodeList<BodyDeclaration<?>> anonymousClassBody) {
      super(tokenRange);
      this.setScope(scope);
      this.setType(type);
      this.setTypeArguments(typeArguments);
      this.setArguments(arguments);
      this.setAnonymousClassBody(anonymousClassBody);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<NodeList<BodyDeclaration<?>>> getAnonymousClassBody() {
      return Optional.ofNullable(this.anonymousClassBody);
   }

   public void addAnonymousClassBody(BodyDeclaration<?> body) {
      if (this.anonymousClassBody == null) {
         this.anonymousClassBody = new NodeList();
      }

      this.anonymousClassBody.add((Node)body);
   }

   public NodeList<Expression> getArguments() {
      return this.arguments;
   }

   public Optional<Expression> getScope() {
      return Optional.ofNullable(this.scope);
   }

   public ClassOrInterfaceType getType() {
      return this.type;
   }

   public ObjectCreationExpr setAnonymousClassBody(final NodeList<BodyDeclaration<?>> anonymousClassBody) {
      if (anonymousClassBody == this.anonymousClassBody) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ANONYMOUS_CLASS_BODY, this.anonymousClassBody, anonymousClassBody);
         if (this.anonymousClassBody != null) {
            this.anonymousClassBody.setParentNode((Node)null);
         }

         this.anonymousClassBody = anonymousClassBody;
         this.setAsParentNodeOf(anonymousClassBody);
         return this;
      }
   }

   public ObjectCreationExpr setArguments(final NodeList<Expression> arguments) {
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

   public ObjectCreationExpr setScope(final Expression scope) {
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

   public ObjectCreationExpr setType(final ClassOrInterfaceType type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         if (this.type != null) {
            this.type.setParentNode((Node)null);
         }

         this.type = type;
         this.setAsParentNodeOf(type);
         return this;
      }
   }

   public Optional<NodeList<Type>> getTypeArguments() {
      return Optional.ofNullable(this.typeArguments);
   }

   public ObjectCreationExpr setTypeArguments(final NodeList<Type> typeArguments) {
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
         if (this.anonymousClassBody != null) {
            for(i = 0; i < this.anonymousClassBody.size(); ++i) {
               if (this.anonymousClassBody.get(i) == node) {
                  this.anonymousClassBody.remove(i);
                  return true;
               }
            }
         }

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

   public ObjectCreationExpr removeScope() {
      return this.setScope((Expression)null);
   }

   public ObjectCreationExpr clone() {
      return (ObjectCreationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ObjectCreationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.objectCreationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         if (this.anonymousClassBody != null) {
            for(i = 0; i < this.anonymousClassBody.size(); ++i) {
               if (this.anonymousClassBody.get(i) == node) {
                  this.anonymousClassBody.set(i, (Node)((BodyDeclaration)replacementNode));
                  return true;
               }
            }
         }

         for(i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == node) {
               this.arguments.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         if (this.scope != null && node == this.scope) {
            this.setScope((Expression)replacementNode);
            return true;
         } else if (node == this.type) {
            this.setType((ClassOrInterfaceType)replacementNode);
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

   public boolean isObjectCreationExpr() {
      return true;
   }

   public ObjectCreationExpr asObjectCreationExpr() {
      return this;
   }

   public void ifObjectCreationExpr(Consumer<ObjectCreationExpr> action) {
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

   public Optional<ObjectCreationExpr> toObjectCreationExpr() {
      return Optional.of(this);
   }
}
