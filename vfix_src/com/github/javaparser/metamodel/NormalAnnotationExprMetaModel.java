package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import java.util.Optional;

public class NormalAnnotationExprMetaModel extends AnnotationExprMetaModel {
   public PropertyMetaModel pairsPropertyMetaModel;

   NormalAnnotationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, NormalAnnotationExpr.class, "NormalAnnotationExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
