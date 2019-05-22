package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import java.util.Optional;

public class BooleanLiteralExprMetaModel extends LiteralExprMetaModel {
   public PropertyMetaModel valuePropertyMetaModel;

   BooleanLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, BooleanLiteralExpr.class, "BooleanLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
