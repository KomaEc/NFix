package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.VariableDeclarator;
import java.util.Optional;

public class VariableDeclaratorMetaModel extends NodeMetaModel {
   public PropertyMetaModel initializerPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   VariableDeclaratorMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, VariableDeclarator.class, "VariableDeclarator", "com.github.javaparser.ast.body", false, false);
   }
}
