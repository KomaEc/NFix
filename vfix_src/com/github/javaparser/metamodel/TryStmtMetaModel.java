package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.TryStmt;
import java.util.Optional;

public class TryStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel catchClausesPropertyMetaModel;
   public PropertyMetaModel finallyBlockPropertyMetaModel;
   public PropertyMetaModel resourcesPropertyMetaModel;
   public PropertyMetaModel tryBlockPropertyMetaModel;

   TryStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, TryStmt.class, "TryStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
