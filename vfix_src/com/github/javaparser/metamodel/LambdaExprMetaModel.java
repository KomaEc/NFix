package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.LambdaExpr;
import java.util.Optional;

public class LambdaExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel isEnclosingParametersPropertyMetaModel;
   public PropertyMetaModel parametersPropertyMetaModel;
   public PropertyMetaModel expressionBodyPropertyMetaModel;

   LambdaExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LambdaExpr.class, "LambdaExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
