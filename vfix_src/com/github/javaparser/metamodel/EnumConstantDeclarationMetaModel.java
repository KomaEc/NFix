package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import java.util.Optional;

public class EnumConstantDeclarationMetaModel extends BodyDeclarationMetaModel {
   public PropertyMetaModel argumentsPropertyMetaModel;
   public PropertyMetaModel classBodyPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   EnumConstantDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, EnumConstantDeclaration.class, "EnumConstantDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
