package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ForeachStmt;
import java.util.Optional;

public class ForeachStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel iterablePropertyMetaModel;
   public PropertyMetaModel variablePropertyMetaModel;

   ForeachStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ForeachStmt.class, "ForeachStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
