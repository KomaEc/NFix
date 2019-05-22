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
import com.github.javaparser.metamodel.ModuleProvidesStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ModuleProvidesStmt extends ModuleStmt implements NodeWithName<ModuleProvidesStmt> {
   private Name name;
   private NodeList<Name> with;

   public ModuleProvidesStmt() {
      this((TokenRange)null, new Name(), new NodeList());
   }

   @AllFieldsConstructor
   public ModuleProvidesStmt(Name name, NodeList<Name> with) {
      this((TokenRange)null, name, with);
   }

   public ModuleProvidesStmt(TokenRange tokenRange, Name name, NodeList<Name> with) {
      super(tokenRange);
      this.setName(name);
      this.setWith(with);
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
         for(int i = 0; i < this.with.size(); ++i) {
            if (this.with.get(i) == node) {
               this.with.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public ModuleProvidesStmt clone() {
      return (ModuleProvidesStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ModuleProvidesStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleProvidesStmtMetaModel;
   }

   public boolean isModuleProvidesStmt() {
      return true;
   }

   public ModuleProvidesStmt asModuleProvidesStmt() {
      return this;
   }

   public void ifModuleProvidesStmt(Consumer<ModuleProvidesStmt> action) {
      action.accept(this);
   }

   public Optional<ModuleProvidesStmt> toModuleProvidesStmt() {
      return Optional.of(this);
   }

   public Name getName() {
      return this.name;
   }

   public ModuleProvidesStmt setName(final Name name) {
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

   public ModuleProvidesStmt setWith(final NodeList<Name> with) {
      Utils.assertNotNull(with);
      if (with == this.with) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.WITH, this.with, with);
         if (this.with != null) {
            this.with.setParentNode((Node)null);
         }

         this.with = with;
         this.setAsParentNodeOf(with);
         return this;
      }
   }

   public NodeList<Name> getWith() {
      return this.with;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((Name)replacementNode);
         return true;
      } else {
         for(int i = 0; i < this.with.size(); ++i) {
            if (this.with.get(i) == node) {
               this.with.set(i, (Node)((Name)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }
}
