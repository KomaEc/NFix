package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.Optional;

public class MethodDeclarationMetaModel extends CallableDeclarationMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   MethodDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, MethodDeclaration.class, "MethodDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
