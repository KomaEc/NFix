package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import java.util.Optional;

public class SwitchEntryStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel labelPropertyMetaModel;
   public PropertyMetaModel statementsPropertyMetaModel;

   SwitchEntryStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, SwitchEntryStmt.class, "SwitchEntryStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
