package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import java.util.Optional;

public class LocalClassDeclarationStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel classDeclarationPropertyMetaModel;

   LocalClassDeclarationStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LocalClassDeclarationStmt.class, "LocalClassDeclarationStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
