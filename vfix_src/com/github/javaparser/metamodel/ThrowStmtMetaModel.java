package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ThrowStmt;
import java.util.Optional;

public class ThrowStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel expressionPropertyMetaModel;

   ThrowStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ThrowStmt.class, "ThrowStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
