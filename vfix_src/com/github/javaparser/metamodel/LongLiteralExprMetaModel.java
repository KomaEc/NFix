package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.LongLiteralExpr;
import java.util.Optional;

public class LongLiteralExprMetaModel extends LiteralStringValueExprMetaModel {
   LongLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LongLiteralExpr.class, "LongLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
