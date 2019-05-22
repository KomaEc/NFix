package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.BlockStmt;
import java.util.Optional;

public class BlockStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel statementsPropertyMetaModel;

   BlockStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, BlockStmt.class, "BlockStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
