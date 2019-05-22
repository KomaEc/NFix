package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.EmptyStmt;
import java.util.Optional;

public class EmptyStmtMetaModel extends StatementMetaModel {
   EmptyStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, EmptyStmt.class, "EmptyStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
