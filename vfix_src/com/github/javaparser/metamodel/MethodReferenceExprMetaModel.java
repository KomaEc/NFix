package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.MethodReferenceExpr;
import java.util.Optional;

public class MethodReferenceExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel identifierPropertyMetaModel;
   public PropertyMetaModel scopePropertyMetaModel;
   public PropertyMetaModel typeArgumentsPropertyMetaModel;
   public PropertyMetaModel usingDiamondOperatorPropertyMetaModel;

   MethodReferenceExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, MethodReferenceExpr.class, "MethodReferenceExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
