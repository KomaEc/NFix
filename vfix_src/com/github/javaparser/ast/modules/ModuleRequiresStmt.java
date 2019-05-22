package com.github.javaparser.ast.modules;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleRequiresStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class ModuleRequiresStmt extends ModuleStmt implements NodeWithStaticModifier<ModuleRequiresStmt>, NodeWithName<ModuleRequiresStmt> {
   private EnumSet<Modifier> modifiers;
   private Name name;

   public ModuleRequiresStmt() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new Name());
   }

   @AllFieldsConstructor
   public ModuleRequiresStmt(EnumSet<Modifier> modifiers, Name name) {
      this((TokenRange)null, modifiers, name);
   }

   public ModuleRequiresStmt(TokenRange tokenRange, EnumSet<Modifier> modifiers, Name name) {
      super(tokenRange);
      this.setModifiers(modifiers);
      this.setName(name);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public ModuleRequiresStmt setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public Name getName() {
      return this.name;
   }

   public ModuleRequiresStmt setName(final Name name) {
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

   public boolean isTransitive() {
      return this.getModifiers().contains(Modifier.TRANSITIVE);
   }

   public ModuleRequiresStmt setTransitive(boolean set) {
      return (ModuleRequiresStmt)this.setModifier(Modifier.TRANSITIVE, set);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ModuleRequiresStmt clone() {
      return (ModuleRequiresStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ModuleRequiresStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleRequiresStmtMetaModel;
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

   public boolean isModuleRequiresStmt() {
      return true;
   }

   public ModuleRequiresStmt asModuleRequiresStmt() {
      return this;
   }

   public void ifModuleRequiresStmt(Consumer<ModuleRequiresStmt> action) {
      action.accept(this);
   }

   public Optional<ModuleRequiresStmt> toModuleRequiresStmt() {
      return Optional.of(this);
   }
}
