package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import java.util.Optional;

public class IntegerLiteralExprMetaModel extends LiteralStringValueExprMetaModel {
   IntegerLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, IntegerLiteralExpr.class, "IntegerLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
