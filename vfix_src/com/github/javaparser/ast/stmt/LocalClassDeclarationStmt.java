package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LocalClassDeclarationStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class LocalClassDeclarationStmt extends Statement {
   private ClassOrInterfaceDeclaration classDeclaration;

   public LocalClassDeclarationStmt() {
      this((TokenRange)null, new ClassOrInterfaceDeclaration());
   }

   @AllFieldsConstructor
   public LocalClassDeclarationStmt(final ClassOrInterfaceDeclaration classDeclaration) {
      this((TokenRange)null, classDeclaration);
   }

   public LocalClassDeclarationStmt(TokenRange tokenRange, ClassOrInterfaceDeclaration classDeclaration) {
      super(tokenRange);
      this.setClassDeclaration(classDeclaration);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public ClassOrInterfaceDeclaration getClassDeclaration() {
      return this.classDeclaration;
   }

   public LocalClassDeclarationStmt setClassDeclaration(final ClassOrInterfaceDeclaration classDeclaration) {
      Utils.assertNotNull(classDeclaration);
      if (classDeclaration == this.classDeclaration) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CLASS_DECLARATION, this.classDeclaration, classDeclaration);
         if (this.classDeclaration != null) {
            this.classDeclaration.setParentNode((Node)null);
         }

         this.classDeclaration = classDeclaration;
         this.setAsParentNodeOf(classDeclaration);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public LocalClassDeclarationStmt clone() {
      return (LocalClassDeclarationStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public LocalClassDeclarationStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.localClassDeclarationStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.classDeclaration) {
         this.setClassDeclaration((ClassOrInterfaceDeclaration)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isLocalClassDeclarationStmt() {
      return true;
   }

   public LocalClassDeclarationStmt asLocalClassDeclarationStmt() {
      return this;
   }

   public void ifLocalClassDeclarationStmt(Consumer<LocalClassDeclarationStmt> action) {
      action.accept(this);
   }

   public Optional<LocalClassDeclarationStmt> toLocalClassDeclarationStmt() {
      return Optional.of(this);
   }
}
