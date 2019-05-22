package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.util.Optional;

public class AnnotationExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel namePropertyMetaModel;

   AnnotationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, AnnotationExpr.class, "AnnotationExpr", "com.github.javaparser.ast.expr", true, false);
   }

   protected AnnotationExprMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
