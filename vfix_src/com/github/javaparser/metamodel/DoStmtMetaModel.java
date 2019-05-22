package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.DoStmt;
import java.util.Optional;

public class DoStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel conditionPropertyMetaModel;

   DoStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, DoStmt.class, "DoStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
