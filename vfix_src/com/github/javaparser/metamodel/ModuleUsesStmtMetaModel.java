package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleUsesStmt;
import java.util.Optional;

public class ModuleUsesStmtMetaModel extends ModuleStmtMetaModel {
   public PropertyMetaModel namePropertyMetaModel;

   ModuleUsesStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleUsesStmt.class, "ModuleUsesStmt", "com.github.javaparser.ast.modules", false, false);
   }
}
