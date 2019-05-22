package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.LiteralExpr;
import java.util.Optional;

public class LiteralExprMetaModel extends ExpressionMetaModel {
   LiteralExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LiteralExpr.class, "LiteralExpr", "com.github.javaparser.ast.expr", true, false);
   }

   protected LiteralExprMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
