package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.NameExpr;
import java.util.Optional;

public class NameExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel namePropertyMetaModel;

   NameExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, NameExpr.class, "NameExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
