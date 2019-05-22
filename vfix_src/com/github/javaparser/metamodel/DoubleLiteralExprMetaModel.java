package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import java.util.Optional;

public class DoubleLiteralExprMetaModel extends LiteralStringValueExprMetaModel {
   DoubleLiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, DoubleLiteralExpr.class, "DoubleLiteralExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
