package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import java.util.Optional;

public class ExpressionStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel expressionPropertyMetaModel;

   ExpressionStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ExpressionStmt.class, "ExpressionStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
