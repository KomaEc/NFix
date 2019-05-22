package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.EnumDeclaration;
import java.util.Optional;

public class EnumDeclarationMetaModel extends TypeDeclarationMetaModel {
   public PropertyMetaModel entriesPropertyMetaModel;
   public PropertyMetaModel implementedTypesPropertyMetaModel;

   EnumDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, EnumDeclaration.class, "EnumDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
