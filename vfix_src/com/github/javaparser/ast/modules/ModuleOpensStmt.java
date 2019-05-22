package com.github.javaparser.ast.modules;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleOpensStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ModuleOpensStmt extends ModuleStmt implements NodeWithName<ModuleOpensStmt> {
   private Name name;
   private NodeList<Name> moduleNames;

   public ModuleOpensStmt() {
      this((TokenRange)null, new Name(), new NodeList());
   }

   @AllFieldsConstructor
   public ModuleOpensStmt(Name name, NodeList<Name> moduleNames) {
      this((TokenRange)null, name, moduleNames);
   }

   public ModuleOpensStmt(TokenRange tokenRange, Name name, NodeList<Name> moduleNames) {
      super(tokenRange);
      this.setName(name);
      this.setModuleNames(moduleNames);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.moduleNames.size(); ++i) {
            if (this.moduleNames.get(i) == node) {
               this.moduleNames.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public Name getName() {
      return this.name;
   }

   public ModuleOpensStmt setName(final Name name) {
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

   public NodeList<Name> getModuleNames() {
      return this.moduleNames;
   }

   public ModuleOpensStmt setModuleNames(final NodeList<Name> moduleNames) {
      Utils.assertNotNull(moduleNames);
      if (moduleNames == this.moduleNames) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODULE_NAMES, this.moduleNames, moduleNames);
         if (this.moduleNames != null) {
            this.moduleNames.setParentNode((Node)null);
         }

         this.moduleNames = moduleNames;
         this.setAsParentNodeOf(moduleNames);
         return this;
      }
   }

   public ModuleOpensStmt clone() {
      return (ModuleOpensStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ModuleOpensStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleOpensStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.moduleNames.size(); ++i) {
            if (this.moduleNames.get(i) == node) {
               this.moduleNames.set(i, (Node)((Name)replacementNode));
               return true;
            }
         }

         if (node == this.name) {
            this.setName((Name)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isModuleOpensStmt() {
      return true;
   }

   public ModuleOpensStmt asModuleOpensStmt() {
      return this;
   }

   public void ifModuleOpensStmt(Consumer<ModuleOpensStmt> action) {
      action.accept(this);
   }

   public Optional<ModuleOpensStmt> toModuleOpensStmt() {
      return Optional.of(this);
   }
}
