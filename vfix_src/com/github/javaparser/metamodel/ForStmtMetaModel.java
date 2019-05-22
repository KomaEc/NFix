package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ForStmt;
import java.util.Optional;

public class ForStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel comparePropertyMetaModel;
   public PropertyMetaModel initializationPropertyMetaModel;
   public PropertyMetaModel updatePropertyMetaModel;

   ForStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ForStmt.class, "ForStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
