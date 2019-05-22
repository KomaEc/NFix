package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.UnparsableStmt;
import java.util.Optional;

public class UnparsableStmtMetaModel extends StatementMetaModel {
   UnparsableStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, UnparsableStmt.class, "UnparsableStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
