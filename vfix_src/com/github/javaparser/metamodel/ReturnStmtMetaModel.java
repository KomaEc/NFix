package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ReturnStmt;
import java.util.Optional;

public class ReturnStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel expressionPropertyMetaModel;

   ReturnStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ReturnStmt.class, "ReturnStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
