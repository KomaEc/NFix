package com.github.javaparser.ast.modules;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleUsesStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ModuleUsesStmt extends ModuleStmt implements NodeWithName<ModuleUsesStmt> {
   private Name name;

   public ModuleUsesStmt() {
      this((TokenRange)null, new Name());
   }

   @AllFieldsConstructor
   public ModuleUsesStmt(Name name) {
      this((TokenRange)null, name);
   }

   public ModuleUsesStmt(TokenRange tokenRange, Name name) {
      super(tokenRange);
      this.setName(name);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ModuleUsesStmt setType(final Name name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public ModuleUsesStmt clone() {
      return (ModuleUsesStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ModuleUsesStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleUsesStmtMetaModel;
   }

   public boolean isModuleUsesStmt() {
      return true;
   }

   public ModuleUsesStmt asModuleUsesStmt() {
      return this;
   }

   public void ifModuleUsesStmt(Consumer<ModuleUsesStmt> action) {
      action.accept(this);
   }

   public Optional<ModuleUsesStmt> toModuleUsesStmt() {
      return Optional.of(this);
   }

   public Name getName() {
      return this.name;
   }

   public ModuleUsesStmt setName(final Name name) {
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
