package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.AssertStmt;
import java.util.Optional;

public class AssertStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel checkPropertyMetaModel;
   public PropertyMetaModel messagePropertyMetaModel;

   AssertStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, AssertStmt.class, "AssertStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
