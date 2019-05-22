package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.ConditionalExpr;
import java.util.Optional;

public class ConditionalExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel conditionPropertyMetaModel;
   public PropertyMetaModel elseExprPropertyMetaModel;
   public PropertyMetaModel thenExprPropertyMetaModel;

   ConditionalExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ConditionalExpr.class, "ConditionalExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
