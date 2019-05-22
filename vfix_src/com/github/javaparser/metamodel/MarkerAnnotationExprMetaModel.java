package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import java.util.Optional;

public class MarkerAnnotationExprMetaModel extends AnnotationExprMetaModel {
   MarkerAnnotationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, MarkerAnnotationExpr.class, "MarkerAnnotationExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
