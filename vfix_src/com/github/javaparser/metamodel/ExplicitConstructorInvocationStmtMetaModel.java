package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import java.util.Optional;

public class ExplicitConstructorInvocationStmtMetaModel extends StatementMetaModel {
   public PropertyMetaModel argumentsPropertyMetaModel;
   public PropertyMetaModel expressionPropertyMetaModel;
   public PropertyMetaModel isThisPropertyMetaModel;
   public PropertyMetaModel typeArgumentsPropertyMetaModel;
   public PropertyMetaModel usingDiamondOperatorPropertyMetaModel;

   ExplicitConstructorInvocationStmtMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ExplicitConstructorInvocationStmt.class, "ExplicitConstructorInvocationStmt", "com.github.javaparser.ast.stmt", false, false);
   }
}
