package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.WhileStmt;
import java.util.Optional;

public class WhileStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel conditionPropertyMetaModel;

   WhileStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, WhileStmt.class, "WhileStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
