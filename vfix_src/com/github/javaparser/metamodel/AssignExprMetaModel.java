package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.AssignExpr;
import java.util.Optional;

public class AssignExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel operatorPropertyMetaModel;
   public PropertyMetaModel targetPropertyMetaModel;
   public PropertyMetaModel valuePropertyMetaModel;

   AssignExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, AssignExpr.class, "AssignExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
