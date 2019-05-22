package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.BodyDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BodyDeclaration<T extends BodyDeclaration<?>> extends Node implements NodeWithAnnotations<T> {
   private NodeList<AnnotationExpr> annotations;

   public BodyDeclaration() {
      this((TokenRange)null, new NodeList());
   }

   @AllFieldsConstructor
   public BodyDeclaration(NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, annotations);
   }

   public BodyDeclaration(TokenRange tokenRange, NodeList<AnnotationExpr> annotations) {
      super(tokenRange);
      this.setAnnotations(annotations);
      this.customInitialization();
   }

   protected BodyDeclaration(TokenRange range) {
      this(range, new NodeList());
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public T setAnnotations(final NodeList<AnnotationExpr> annotations) {
      Utils.assertNotNull(annotations);
      if (annotations == this.annotations) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ANNOTATIONS, this.annotations, annotations);
         if (this.annotations != null) {
            this.annotations.setParentNode((Node)null);
         }

         this.annotations = annotations;
         this.setAsParentNodeOf(annotations);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public BodyDeclaration<?> clone() {
      return (BodyDeclaration)this.accept(new CloneVisitor(), (Object)null);
   }

   public BodyDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.bodyDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.set(i, (Node)((AnnotationExpr)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isAnnotationDeclaration() {
      return false;
   }

   public AnnotationDeclaration asAnnotationDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an AnnotationDeclaration", this));
   }

   public boolean isAnnotationMemberDeclaration() {
      return false;
   }

   public AnnotationMemberDeclaration asAnnotationMemberDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an AnnotationMemberDeclaration", this));
   }

   public boolean isCallableDeclaration() {
      return false;
   }

   public CallableDeclaration asCallableDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an CallableDeclaration", this));
   }

   public boolean isClassOrInterfaceDeclaration() {
      return false;
   }

   public ClassOrInterfaceDeclaration asClassOrInterfaceDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ClassOrInterfaceDeclaration", this));
   }

   public boolean isConstructorDeclaration() {
      return false;
   }

   public ConstructorDeclaration asConstructorDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ConstructorDeclaration", this));
   }

   public boolean isEnumConstantDeclaration() {
      return false;
   }

   public EnumConstantDeclaration asEnumConstantDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an EnumConstantDeclaration", this));
   }

   public boolean isEnumDeclaration() {
      return false;
   }

   public EnumDeclaration asEnumDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an EnumDeclaration", this));
   }

   public boolean isFieldDeclaration() {
      return false;
   }

   public FieldDeclaration asFieldDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an FieldDeclaration", this));
   }

   public boolean isInitializerDeclaration() {
      return false;
   }

   public InitializerDeclaration asInitializerDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an InitializerDeclaration", this));
   }

   public boolean isMethodDeclaration() {
      return false;
   }

   public MethodDeclaration asMethodDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an MethodDeclaration", this));
   }

   public boolean isTypeDeclaration() {
      return false;
   }

   public TypeDeclaration asTypeDeclaration() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an TypeDeclaration", this));
   }

   public void ifAnnotationDeclaration(Consumer<AnnotationDeclaration> action) {
   }

   public void ifAnnotationMemberDeclaration(Consumer<AnnotationMemberDeclaration> action) {
   }

   public void ifCallableDeclaration(Consumer<CallableDeclaration> action) {
   }

   public void ifClassOrInterfaceDeclaration(Consumer<ClassOrInterfaceDeclaration> action) {
   }

   public void ifConstructorDeclaration(Consumer<ConstructorDeclaration> action) {
   }

   public void ifEnumConstantDeclaration(Consumer<EnumConstantDeclaration> action) {
   }

   public void ifEnumDeclaration(Consumer<EnumDeclaration> action) {
   }

   public void ifFieldDeclaration(Consumer<FieldDeclaration> action) {
   }

   public void ifInitializerDeclaration(Consumer<InitializerDeclaration> action) {
   }

   public void ifMethodDeclaration(Consumer<MethodDeclaration> action) {
   }

   public void ifTypeDeclaration(Consumer<TypeDeclaration> action) {
   }

   public Optional<AnnotationDeclaration> toAnnotationDeclaration() {
      return Optional.empty();
   }

   public Optional<AnnotationMemberDeclaration> toAnnotationMemberDeclaration() {
      return Optional.empty();
   }

   public Optional<CallableDeclaration> toCallableDeclaration() {
      return Optional.empty();
   }

   public Optional<ClassOrInterfaceDeclaration> toClassOrInterfaceDeclaration() {
      return Optional.empty();
   }

   public Optional<ConstructorDeclaration> toConstructorDeclaration() {
      return Optional.empty();
   }

   public Optional<EnumConstantDeclaration> toEnumConstantDeclaration() {
      return Optional.empty();
   }

   public Optional<EnumDeclaration> toEnumDeclaration() {
      return Optional.empty();
   }

   public Optional<FieldDeclaration> toFieldDeclaration() {
      return Optional.empty();
   }

   public Optional<InitializerDeclaration> toInitializerDeclaration() {
      return Optional.empty();
   }

   public Optional<MethodDeclaration> toMethodDeclaration() {
      return Optional.empty();
   }

   public Optional<TypeDeclaration> toTypeDeclaration() {
      return Optional.empty();
   }
}
