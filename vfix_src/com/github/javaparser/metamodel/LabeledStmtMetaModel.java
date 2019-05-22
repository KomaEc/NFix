package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.LabeledStmt;
import java.util.Optional;

public class LabeledStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel labelPropertyMetaModel;
   public PropertyMetaModel statementPropertyMetaModel;

   LabeledStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LabeledStmt.class, "LabeledStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
