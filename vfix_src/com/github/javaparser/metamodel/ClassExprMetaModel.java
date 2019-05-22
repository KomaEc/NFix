package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.ClassExpr;
import java.util.Optional;

public class ClassExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel typePropertyMetaModel;

   ClassExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ClassExpr.class, "ClassExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
