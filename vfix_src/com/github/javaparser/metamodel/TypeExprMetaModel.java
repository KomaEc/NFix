package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.TypeExpr;
import java.util.Optional;

public class TypeExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel typePropertyMetaModel;

   TypeExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, TypeExpr.class, "TypeExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
