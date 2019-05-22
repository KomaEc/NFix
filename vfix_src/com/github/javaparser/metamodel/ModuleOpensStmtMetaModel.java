package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleOpensStmt;
import java.util.Optional;

public class ModuleOpensStmtMetaModel extends ModuleStmtMetaModel {
   public PropertyMetaModel moduleNamesPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   ModuleOpensStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleOpensStmt.class, "ModuleOpensStmt", "com.github.javaparser.ast.modules", false, false);
   }
}
