package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import com.github.javaparser.ast.nodeTypes.NodeWithExtends;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ClassOrInterfaceDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class ClassOrInterfaceDeclaration extends TypeDeclaration<ClassOrInterfaceDeclaration> implements NodeWithImplements<ClassOrInterfaceDeclaration>, NodeWithExtends<ClassOrInterfaceDeclaration>, NodeWithTypeParameters<ClassOrInterfaceDeclaration>, NodeWithAbstractModifier<ClassOrInterfaceDeclaration>, NodeWithFinalModifier<ClassOrInterfaceDeclaration>, NodeWithConstructors<ClassOrInterfaceDeclaration>, Resolvable<ResolvedReferenceTypeDeclaration> {
   private boolean isInterface;
   private NodeList<TypeParameter> typeParameters;
   private NodeList<ClassOrInterfaceType> extendedTypes;
   private NodeList<ClassOrInterfaceType> implementedTypes;

   public ClassOrInterfaceDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), false, new SimpleName(), new NodeList(), new NodeList(), new NodeList(), new NodeList());
   }

   public ClassOrInterfaceDeclaration(final EnumSet<Modifier> modifiers, final boolean isInterface, final String name) {
      this((TokenRange)null, modifiers, new NodeList(), isInterface, new SimpleName(name), new NodeList(), new NodeList(), new NodeList(), new NodeList());
   }

   @AllFieldsConstructor
   public ClassOrInterfaceDeclaration(final EnumSet<Modifier> modifiers, final NodeList<AnnotationExpr> annotations, final boolean isInterface, final SimpleName name, final NodeList<TypeParameter> typeParameters, final NodeList<ClassOrInterfaceType> extendedTypes, final NodeList<ClassOrInterfaceType> implementedTypes, final NodeList<BodyDeclaration<?>> members) {
      this((TokenRange)null, modifiers, annotations, isInterface, name, typeParameters, extendedTypes, implementedTypes, members);
   }

   public ClassOrInterfaceDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, boolean isInterface, SimpleName name, NodeList<TypeParameter> typeParameters, NodeList<ClassOrInterfaceType> extendedTypes, NodeList<ClassOrInterfaceType> implementedTypes, NodeList<BodyDeclaration<?>> members) {
      super(tokenRange, modifiers, annotations, name, members);
      this.setInterface(isInterface);
      this.setTypeParameters(typeParameters);
      this.setExtendedTypes(extendedTypes);
      this.setImplementedTypes(implementedTypes);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<ClassOrInterfaceType> getExtendedTypes() {
      return this.extendedTypes;
   }

   public NodeList<ClassOrInterfaceType> getImplementedTypes() {
      return this.implementedTypes;
   }

   public NodeList<TypeParameter> getTypeParameters() {
      return this.typeParameters;
   }

   public boolean isInterface() {
      return this.isInterface;
   }

   public ClassOrInterfaceDeclaration setExtendedTypes(final NodeList<ClassOrInterfaceType> extendedTypes) {
      Utils.assertNotNull(extendedTypes);
      if (extendedTypes == this.extendedTypes) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.EXTENDED_TYPES, this.extendedTypes, extendedTypes);
         if (this.extendedTypes != null) {
            this.extendedTypes.setParentNode((Node)null);
         }

         this.extendedTypes = extendedTypes;
         this.setAsParentNodeOf(extendedTypes);
         return this;
      }
   }

   public ClassOrInterfaceDeclaration setImplementedTypes(final NodeList<ClassOrInterfaceType> implementedTypes) {
      Utils.assertNotNull(implementedTypes);
      if (implementedTypes == this.implementedTypes) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.IMPLEMENTED_TYPES, this.implementedTypes, implementedTypes);
         if (this.implementedTypes != null) {
            this.implementedTypes.setParentNode((Node)null);
         }

         this.implementedTypes = implementedTypes;
         this.setAsParentNodeOf(implementedTypes);
         return this;
      }
   }

   public ClassOrInterfaceDeclaration setInterface(final boolean isInterface) {
      if (isInterface == this.isInterface) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INTERFACE, this.isInterface, isInterface);
         this.isInterface = isInterface;
         return this;
      }
   }

   public ClassOrInterfaceDeclaration setTypeParameters(final NodeList<TypeParameter> typeParameters) {
      Utils.assertNotNull(typeParameters);
      if (typeParameters == this.typeParameters) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE_PARAMETERS, this.typeParameters, typeParameters);
         if (this.typeParameters != null) {
            this.typeParameters.setParentNode((Node)null);
         }

         this.typeParameters = typeParameters;
         this.setAsParentNodeOf(typeParameters);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.extendedTypes.size(); ++i) {
            if (this.extendedTypes.get(i) == node) {
               this.extendedTypes.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.implementedTypes.size(); ++i) {
            if (this.implementedTypes.get(i) == node) {
               this.implementedTypes.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.typeParameters.size(); ++i) {
            if (this.typeParameters.get(i) == node) {
               this.typeParameters.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public boolean isLocalClassDeclaration() {
      return (Boolean)this.getParentNode().map((p) -> {
         return p instanceof LocalClassDeclarationStmt;
      }).orElse(false);
   }

   public boolean isInnerClass() {
      return this.isNestedType() && !this.isInterface && !this.isStatic();
   }

   public ClassOrInterfaceDeclaration clone() {
      return (ClassOrInterfaceDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ClassOrInterfaceDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.classOrInterfaceDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.extendedTypes.size(); ++i) {
            if (this.extendedTypes.get(i) == node) {
               this.extendedTypes.set(i, (Node)((ClassOrInterfaceType)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.implementedTypes.size(); ++i) {
            if (this.implementedTypes.get(i) == node) {
               this.implementedTypes.set(i, (Node)((ClassOrInterfaceType)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.typeParameters.size(); ++i) {
            if (this.typeParameters.get(i) == node) {
               this.typeParameters.set(i, (Node)((TypeParameter)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isClassOrInterfaceDeclaration() {
      return true;
   }

   public ClassOrInterfaceDeclaration asClassOrInterfaceDeclaration() {
      return this;
   }

   public void ifClassOrInterfaceDeclaration(Consumer<ClassOrInterfaceDeclaration> action) {
      action.accept(this);
   }

   public ResolvedReferenceTypeDeclaration resolve() {
      return (ResolvedReferenceTypeDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedReferenceTypeDeclaration.class);
   }

   public Optional<ClassOrInterfaceDeclaration> toClassOrInterfaceDeclaration() {
      return Optional.of(this);
   }
}
