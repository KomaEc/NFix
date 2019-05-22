package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.CastExpr;
import java.util.Optional;

public class CastExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel expressionPropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   CastExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, CastExpr.class, "CastExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
