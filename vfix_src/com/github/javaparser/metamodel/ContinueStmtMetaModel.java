package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ContinueStmt;
import java.util.Optional;

public class ContinueStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel labelPropertyMetaModel;

   ContinueStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ContinueStmt.class, "ContinueStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
