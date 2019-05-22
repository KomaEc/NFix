package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.EnclosedExpr;
import java.util.Optional;

public class EnclosedExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel innerPropertyMetaModel;

   EnclosedExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, EnclosedExpr.class, "EnclosedExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
