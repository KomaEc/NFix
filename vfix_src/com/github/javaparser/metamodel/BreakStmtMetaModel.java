package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.BreakStmt;
import java.util.Optional;

public class BreakStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel labelPropertyMetaModel;

   BreakStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, BreakStmt.class, "BreakStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
