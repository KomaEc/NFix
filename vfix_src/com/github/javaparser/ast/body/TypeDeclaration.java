package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStrictfpModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.TypeDeclarationMetaModel;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class TypeDeclaration<T extends TypeDeclaration<?>> extends BodyDeclaration<T> implements NodeWithSimpleName<T>, NodeWithJavadoc<T>, NodeWithMembers<T>, NodeWithAccessModifiers<T>, NodeWithStaticModifier<T>, NodeWithStrictfpModifier<T> {
   private SimpleName name;
   private EnumSet<Modifier> modifiers;
   private NodeList<BodyDeclaration<?>> members;

   public TypeDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new SimpleName(), new NodeList());
   }

   public TypeDeclaration(EnumSet<Modifier> modifiers, String name) {
      this((TokenRange)null, modifiers, new NodeList(), new SimpleName(name), new NodeList());
   }

   @AllFieldsConstructor
   public TypeDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<BodyDeclaration<?>> members) {
      this((TokenRange)null, modifiers, annotations, name, members);
   }

   public TypeDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<BodyDeclaration<?>> members) {
      super(tokenRange, annotations);
      this.setModifiers(modifiers);
      this.setName(name);
      this.setMembers(members);
      this.customInitialization();
   }

   public T addMember(BodyDeclaration<?> decl) {
      NodeList<BodyDeclaration<?>> members = this.getMembers();
      members.add((Node)decl);
      return this;
   }

   public NodeList<BodyDeclaration<?>> getMembers() {
      return this.members;
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public T setMembers(final NodeList<BodyDeclaration<?>> members) {
      Utils.assertNotNull(members);
      if (members == this.members) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MEMBERS, this.members, members);
         if (this.members != null) {
            this.members.setParentNode((Node)null);
         }

         this.members = members;
         this.setAsParentNodeOf(members);
         return this;
      }
   }

   public T setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public T setName(final SimpleName name) {
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

   public SimpleName getName() {
      return this.name;
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i) == node) {
               this.members.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public boolean isTopLevelType() {
      return (Boolean)this.getParentNode().map((p) -> {
         return p instanceof CompilationUnit;
      }).orElse(false);
   }

   public List<CallableDeclaration<?>> getCallablesWithSignature(CallableDeclaration.Signature signature) {
      return (List)this.getMembers().stream().filter((m) -> {
         return m instanceof CallableDeclaration;
      }).map((m) -> {
         return (CallableDeclaration)m;
      }).filter((m) -> {
         return m.getSignature().equals(signature);
      }).collect(Collectors.toList());
   }

   public boolean isNestedType() {
      return (Boolean)this.getParentNode().map((p) -> {
         return p instanceof TypeDeclaration;
      }).orElse(false);
   }

   public TypeDeclaration<?> clone() {
      return (TypeDeclaration)this.accept(new CloneVisitor(), (Object)null);
   }

   public TypeDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.typeDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i) == node) {
               this.members.set(i, (Node)((BodyDeclaration)replacementNode));
               return true;
            }
         }

         if (node == this.name) {
            this.setName((SimpleName)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isTypeDeclaration() {
      return true;
   }

   public TypeDeclaration asTypeDeclaration() {
      return this;
   }

   public void ifTypeDeclaration(Consumer<TypeDeclaration> action) {
      action.accept(this);
   }

   public Optional<TypeDeclaration> toTypeDeclaration() {
      return Optional.of(this);
   }

   public abstract ResolvedReferenceTypeDeclaration resolve();
}
