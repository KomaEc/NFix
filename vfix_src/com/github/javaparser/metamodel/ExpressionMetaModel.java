package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import java.util.Optional;

public class ExpressionMetaModel extends NodeMetaModel {
   ExpressionMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, Expression.class, "Expression", "com.github.javaparser.ast.expr", true, false);
   }

   protected ExpressionMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
