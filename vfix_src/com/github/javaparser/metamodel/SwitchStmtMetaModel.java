package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.SwitchStmt;
import java.util.Optional;

public class SwitchStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel entriesPropertyMetaModel;
   public PropertyMetaModel selectorPropertyMetaModel;

   SwitchStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, SwitchStmt.class, "SwitchStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
