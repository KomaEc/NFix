package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.BinaryExpr;
import java.util.Optional;

public class BinaryExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel leftPropertyMetaModel;
   public PropertyMetaModel operatorPropertyMetaModel;
   public PropertyMetaModel rightPropertyMetaModel;

   BinaryExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, BinaryExpr.class, "BinaryExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
