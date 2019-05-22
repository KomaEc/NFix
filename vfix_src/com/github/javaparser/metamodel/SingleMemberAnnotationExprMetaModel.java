package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import java.util.Optional;

public class SingleMemberAnnotationExprMetaModel extends AnnotationExprMetaModel {
   public PropertyMetaModel memberValuePropertyMetaModel;

   SingleMemberAnnotationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, SingleMemberAnnotationExpr.class, "SingleMemberAnnotationExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
