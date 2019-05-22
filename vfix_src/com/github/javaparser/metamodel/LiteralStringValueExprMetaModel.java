package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import java.util.Optional;

public class LiteralStringValueExprMetaModel extends LiteralExprMetaModel {
   public PropertyMetaModel valuePropertyMetaModel;

   LiteralStringValueExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, LiteralStringValueExpr.class, "LiteralStringValueExpr", "com.github.javaparser.ast.expr", true, false);
   }

   protected LiteralStringValueExprMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
