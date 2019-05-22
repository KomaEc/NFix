package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.ThisExpr;
import java.util.Optional;

public class ThisExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel classExprPropertyMetaModel;

   ThisExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ThisExpr.class, "ThisExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
