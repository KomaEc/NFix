package com.github.javaparser.ast;

import com.github.javaparser.JavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ImportDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;

public final class ImportDeclaration extends Node implements NodeWithName<ImportDeclaration> {
   private Name name;
   private boolean isStatic;
   private boolean isAsterisk;

   private ImportDeclaration() {
      this((TokenRange)null, new Name(), false, false);
   }

   public ImportDeclaration(String name, boolean isStatic, boolean isAsterisk) {
      this((TokenRange)null, JavaParser.parseName(name), isStatic, isAsterisk);
   }

   @AllFieldsConstructor
   public ImportDeclaration(Name name, boolean isStatic, boolean isAsterisk) {
      this((TokenRange)null, name, isStatic, isAsterisk);
   }

   public ImportDeclaration(TokenRange tokenRange, Name name, boolean isStatic, boolean isAsterisk) {
      super(tokenRange);
      this.setName(name);
      this.setStatic(isStatic);
      this.setAsterisk(isAsterisk);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Name getName() {
      return this.name;
   }

   public boolean isAsterisk() {
      return this.isAsterisk;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   public ImportDeclaration setAsterisk(final boolean isAsterisk) {
      if (isAsterisk == this.isAsterisk) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ASTERISK, this.isAsterisk, isAsterisk);
         this.isAsterisk = isAsterisk;
         return this;
      }
   }

   public ImportDeclaration setName(final Name name) {
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

   public ImportDeclaration setStatic(final boolean isStatic) {
      if (isStatic == this.isStatic) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.STATIC, this.isStatic, isStatic);
         this.isStatic = isStatic;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ImportDeclaration clone() {
      return (ImportDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ImportDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.importDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((Name)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }
}
