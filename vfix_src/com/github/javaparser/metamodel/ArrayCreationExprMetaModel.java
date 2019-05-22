package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.ArrayCreationExpr;
import java.util.Optional;

public class ArrayCreationExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel elementTypePropertyMetaModel;
   public PropertyMetaModel initializerPropertyMetaModel;
   public PropertyMetaModel levelsPropertyMetaModel;

   ArrayCreationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ArrayCreationExpr.class, "ArrayCreationExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
