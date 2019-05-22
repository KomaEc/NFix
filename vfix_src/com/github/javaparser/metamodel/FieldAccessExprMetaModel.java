package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import java.util.Optional;

public class FieldAccessExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel scopePropertyMetaModel;
   public PropertyMetaModel typeArgumentsPropertyMetaModel;
   public PropertyMetaModel usingDiamondOperatorPropertyMetaModel;

   FieldAccessExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, FieldAccessExpr.class, "FieldAccessExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
