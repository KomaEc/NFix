package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.EnumDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class EnumDeclaration extends TypeDeclaration<EnumDeclaration> implements NodeWithImplements<EnumDeclaration>, NodeWithConstructors<EnumDeclaration>, Resolvable<ResolvedEnumDeclaration> {
   private NodeList<ClassOrInterfaceType> implementedTypes;
   private NodeList<EnumConstantDeclaration> entries;

   public EnumDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new SimpleName(), new NodeList(), new NodeList(), new NodeList());
   }

   public EnumDeclaration(EnumSet<Modifier> modifiers, String name) {
      this((TokenRange)null, modifiers, new NodeList(), new SimpleName(name), new NodeList(), new NodeList(), new NodeList());
   }

   @AllFieldsConstructor
   public EnumDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<ClassOrInterfaceType> implementedTypes, NodeList<EnumConstantDeclaration> entries, NodeList<BodyDeclaration<?>> members) {
      this((TokenRange)null, modifiers, annotations, name, implementedTypes, entries, members);
   }

   public EnumDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<ClassOrInterfaceType> implementedTypes, NodeList<EnumConstantDeclaration> entries, NodeList<BodyDeclaration<?>> members) {
      super(tokenRange, modifiers, annotations, name, members);
      this.setImplementedTypes(implementedTypes);
      this.setEntries(entries);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<EnumConstantDeclaration> getEntries() {
      return this.entries;
   }

   public EnumConstantDeclaration getEntry(int i) {
      return (EnumConstantDeclaration)this.getEntries().get(i);
   }

   public EnumDeclaration setEntry(int i, EnumConstantDeclaration element) {
      this.getEntries().set(i, (Node)element);
      return this;
   }

   public EnumDeclaration addEntry(EnumConstantDeclaration element) {
      this.getEntries().add((Node)element);
      return this;
   }

   public NodeList<ClassOrInterfaceType> getImplementedTypes() {
      return this.implementedTypes;
   }

   public EnumDeclaration setEntries(final NodeList<EnumConstantDeclaration> entries) {
      Utils.assertNotNull(entries);
      if (entries == this.entries) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ENTRIES, this.entries, entries);
         if (this.entries != null) {
            this.entries.setParentNode((Node)null);
         }

         this.entries = entries;
         this.setAsParentNodeOf(entries);
         return this;
      }
   }

   public EnumDeclaration setImplementedTypes(final NodeList<ClassOrInterfaceType> implementedTypes) {
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

   public EnumConstantDeclaration addEnumConstant(String name) {
      Utils.assertNonEmpty(name);
      EnumConstantDeclaration enumConstant = new EnumConstantDeclaration(name);
      this.getEntries().add((Node)enumConstant);
      return enumConstant;
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.entries.size(); ++i) {
            if (this.entries.get(i) == node) {
               this.entries.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.implementedTypes.size(); ++i) {
            if (this.implementedTypes.get(i) == node) {
               this.implementedTypes.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public EnumDeclaration clone() {
      return (EnumDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public EnumDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.enumDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.entries.size(); ++i) {
            if (this.entries.get(i) == node) {
               this.entries.set(i, (Node)((EnumConstantDeclaration)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.implementedTypes.size(); ++i) {
            if (this.implementedTypes.get(i) == node) {
               this.implementedTypes.set(i, (Node)((ClassOrInterfaceType)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isEnumDeclaration() {
      return true;
   }

   public EnumDeclaration asEnumDeclaration() {
      return this;
   }

   public void ifEnumDeclaration(Consumer<EnumDeclaration> action) {
      action.accept(this);
   }

   public ResolvedEnumDeclaration resolve() {
      return (ResolvedEnumDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedEnumDeclaration.class);
   }

   public Optional<EnumDeclaration> toEnumDeclaration() {
      return Optional.of(this);
   }
}
