package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ClassOrInterfaceTypeMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class ClassOrInterfaceType extends ReferenceType implements NodeWithSimpleName<ClassOrInterfaceType>, NodeWithAnnotations<ClassOrInterfaceType>, NodeWithTypeArguments<ClassOrInterfaceType> {
   @OptionalProperty
   private ClassOrInterfaceType scope;
   private SimpleName name;
   @OptionalProperty
   private NodeList<Type> typeArguments;

   public ClassOrInterfaceType() {
      this((TokenRange)null, (ClassOrInterfaceType)null, new SimpleName(), (NodeList)null, new NodeList());
   }

   /** @deprecated */
   public ClassOrInterfaceType(final String name) {
      this((TokenRange)null, (ClassOrInterfaceType)null, new SimpleName(name), (NodeList)null, new NodeList());
   }

   public ClassOrInterfaceType(final ClassOrInterfaceType scope, final String name) {
      this((TokenRange)null, scope, new SimpleName(name), (NodeList)null, new NodeList());
   }

   public ClassOrInterfaceType(final ClassOrInterfaceType scope, final SimpleName name, final NodeList<Type> typeArguments) {
      this((TokenRange)null, scope, name, typeArguments, new NodeList());
   }

   @AllFieldsConstructor
   public ClassOrInterfaceType(final ClassOrInterfaceType scope, final SimpleName name, final NodeList<Type> typeArguments, final NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, scope, name, typeArguments, annotations);
   }

   public ClassOrInterfaceType(TokenRange tokenRange, ClassOrInterfaceType scope, SimpleName name, NodeList<Type> typeArguments, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.setScope(scope);
      this.setName(name);
      this.setTypeArguments(typeArguments);
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

   public Optional<ClassOrInterfaceType> getScope() {
      return Optional.ofNullable(this.scope);
   }

   public boolean isBoxedType() {
      return PrimitiveType.unboxMap.containsKey(this.name.getIdentifier());
   }

   public PrimitiveType toUnboxedType() throws UnsupportedOperationException {
      if (!this.isBoxedType()) {
         throw new UnsupportedOperationException(this.name + " isn't a boxed type.");
      } else {
         return new PrimitiveType((PrimitiveType.Primitive)PrimitiveType.unboxMap.get(this.name.getIdentifier()));
      }
   }

   public ClassOrInterfaceType setName(final SimpleName name) {
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

   public ClassOrInterfaceType setScope(final ClassOrInterfaceType scope) {
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

   public ClassOrInterfaceType setTypeArguments(final NodeList<Type> typeArguments) {
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

   public ClassOrInterfaceType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (ClassOrInterfaceType)super.setAnnotations(annotations);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.scope != null && node == this.scope) {
         this.removeScope();
         return true;
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

   public String asString() {
      StringBuilder str = new StringBuilder();
      this.getScope().ifPresent((s) -> {
         str.append(s.asString()).append(".");
      });
      str.append(this.name.asString());
      this.getTypeArguments().ifPresent((ta) -> {
         str.append((String)ta.stream().map(Type::asString).collect(Collectors.joining(",", "<", ">")));
      });
      return str.toString();
   }

   public ClassOrInterfaceType removeScope() {
      return this.setScope((ClassOrInterfaceType)null);
   }

   public ClassOrInterfaceType clone() {
      return (ClassOrInterfaceType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ClassOrInterfaceTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.classOrInterfaceTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else if (this.scope != null && node == this.scope) {
         this.setScope((ClassOrInterfaceType)replacementNode);
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

   public boolean isClassOrInterfaceType() {
      return true;
   }

   public ClassOrInterfaceType asClassOrInterfaceType() {
      return this;
   }

   public void ifClassOrInterfaceType(Consumer<ClassOrInterfaceType> action) {
      action.accept(this);
   }

   public ResolvedReferenceType resolve() {
      return (ResolvedReferenceType)this.getSymbolResolver().toResolvedType(this, ResolvedReferenceType.class);
   }

   public Optional<ClassOrInterfaceType> toClassOrInterfaceType() {
      return Optional.of(this);
   }
}
