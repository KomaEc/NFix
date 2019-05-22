package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.modules.ModuleStmt;
import java.util.Optional;

public class ModuleStmtMetaModel extends NodeMetaModel {
   ModuleStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleStmt.class, "ModuleStmt", "com.github.javaparser.ast.modules", true, false);
   }

   protected ModuleStmtMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
