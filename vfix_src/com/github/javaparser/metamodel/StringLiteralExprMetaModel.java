package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import java.util.Optional;

public class StringLiteralExprMetaModel extends LiteralStringValueExprMetaModel {
   StringLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, StringLiteralExpr.class, "StringLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
