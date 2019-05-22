package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import java.util.Optional;

public class ArrayInitializerExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel valuesPropertyMetaModel;

   ArrayInitializerExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ArrayInitializerExpr.class, "ArrayInitializerExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
