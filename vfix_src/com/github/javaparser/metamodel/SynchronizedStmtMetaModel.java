package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.SynchronizedStmt;
import java.util.Optional;

public class SynchronizedStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel expressionPropertyMetaModel;

   SynchronizedStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, SynchronizedStmt.class, "SynchronizedStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
