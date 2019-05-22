package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import java.util.Optional;

public class ModuleRequiresStmtMetaModel extends ModuleStmtMetaModel {
   public PropertyMetaModel modifiersPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   ModuleRequiresStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleRequiresStmt.class, "ModuleRequiresStmt", "com.github.javaparser.ast.modules", false, false);
   }
}
