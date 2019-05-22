package com.github.javaparser.ast.modules;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleStmtMetaModel;
import com.github.javaparser.utils.CodeGenerationUtils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ModuleStmt extends Node {
   @AllFieldsConstructor
   public ModuleStmt() {
      this((TokenRange)null);
   }

   public ModuleStmt(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ModuleStmt clone() {
      return (ModuleStmt)this.accept(new CloneVisitor(), (Object)null);
   }

   public ModuleStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isModuleExportsStmt() {
      return false;
   }

   public ModuleExportsStmt asModuleExportsStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ModuleExportsStmt", this));
   }

   public boolean isModuleOpensStmt() {
      return false;
   }

   public ModuleOpensStmt asModuleOpensStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ModuleOpensStmt", this));
   }

   public boolean isModuleProvidesStmt() {
      return false;
   }

   public ModuleProvidesStmt asModuleProvidesStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ModuleProvidesStmt", this));
   }

   public boolean isModuleRequiresStmt() {
      return false;
   }

   public ModuleRequiresStmt asModuleRequiresStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ModuleRequiresStmt", this));
   }

   public boolean isModuleUsesStmt() {
      return false;
   }

   public ModuleUsesStmt asModuleUsesStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ModuleUsesStmt", this));
   }

   public void ifModuleExportsStmt(Consumer<ModuleExportsStmt> action) {
   }

   public void ifModuleOpensStmt(Consumer<ModuleOpensStmt> action) {
   }

   public void ifModuleProvidesStmt(Consumer<ModuleProvidesStmt> action) {
   }

   public void ifModuleRequiresStmt(Consumer<ModuleRequiresStmt> action) {
   }

   public void ifModuleUsesStmt(Consumer<ModuleUsesStmt> action) {
   }

   public Optional<ModuleExportsStmt> toModuleExportsStmt() {
      return Optional.empty();
   }

   public Optional<ModuleOpensStmt> toModuleOpensStmt() {
      return Optional.empty();
   }

   public Optional<ModuleProvidesStmt> toModuleProvidesStmt() {
      return Optional.empty();
   }

   public Optional<ModuleRequiresStmt> toModuleRequiresStmt() {
      return Optional.empty();
   }

   public Optional<ModuleUsesStmt> toModuleUsesStmt() {
      return Optional.empty();
   }
}
