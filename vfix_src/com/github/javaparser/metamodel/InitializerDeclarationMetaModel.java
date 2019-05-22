package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.InitializerDeclaration;
import java.util.Optional;

public class InitializerDeclarationMetaModel extends BodyDeclarationMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel isStaticPropertyMetaModel;

   InitializerDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, InitializerDeclaration.class, "InitializerDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
