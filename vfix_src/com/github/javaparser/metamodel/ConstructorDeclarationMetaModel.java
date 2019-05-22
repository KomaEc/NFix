package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import java.util.Optional;

public class ConstructorDeclarationMetaModel extends CallableDeclarationMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;

   ConstructorDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ConstructorDeclaration.class, "ConstructorDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
