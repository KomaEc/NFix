package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import java.util.Optional;

public class ModuleProvidesStmtMetaModel extends ModuleStmtMetaModel {
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel withPropertyMetaModel;

   ModuleProvidesStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleProvidesStmt.class, "ModuleProvidesStmt", "com.github.javaparser.ast.modules", false, false);
   }
}
