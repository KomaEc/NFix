package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.MethodCallExpr;
import java.util.Optional;

public class MethodCallExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel argumentsPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel scopePropertyMetaModel;
   public PropertyMetaModel typeArgumentsPropertyMetaModel;
   public PropertyMetaModel usingDiamondOperatorPropertyMetaModel;

   MethodCallExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, MethodCallExpr.class, "MethodCallExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
