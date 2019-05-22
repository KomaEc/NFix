package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import java.util.Optional;

public class VariableDeclarationExprMetaModel extends ExpressionMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;
   public PropertyMetaModel modifiersPropertyMetaModel;
   public PropertyMetaModel variablesPropertyMetaModel;
   public PropertyMetaModel maximumCommonTypePropertyMetaModel;

   VariableDeclarationExprMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, VariableDeclarationExpr.class, "VariableDeclarationExpr", "com.github.javaparser.ast.expr", false, false);
   }
}
