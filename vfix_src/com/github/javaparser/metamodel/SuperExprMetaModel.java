package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.SuperExpr;
import java.util.Optional;

public class SuperExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel classExprPropertyMetaModel;

   SuperExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, SuperExpr.class, "SuperExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
