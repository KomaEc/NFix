package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import java.util.Optional;

public class NullLiteralExprMetaModel extends LiteralExprMetaModel {
   NullLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, NullLiteralExpr.class, "NullLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
