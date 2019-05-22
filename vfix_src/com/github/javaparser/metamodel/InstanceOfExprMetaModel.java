package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.InstanceOfExpr;
import java.util.Optional;

public class InstanceOfExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel expressionPropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   InstanceOfExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, InstanceOfExpr.class, "InstanceOfExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
