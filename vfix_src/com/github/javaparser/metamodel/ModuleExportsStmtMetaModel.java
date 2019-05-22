package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleExportsStmt;
import java.util.Optional;

public class ModuleExportsStmtMetaModel extends ModuleStmtMetaModel {
   public PropertyMetaModel moduleNamesPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   ModuleExportsStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleExportsStmt.class, "ModuleExportsStmt", "com.github.javaparser.ast.modules", false, false);
   }
}
