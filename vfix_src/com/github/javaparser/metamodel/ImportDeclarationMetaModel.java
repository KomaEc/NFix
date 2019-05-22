package com.github.javaparser.metamodel;

import com.github.javaparser.ast.ImportDeclaration;
import java.util.Optional;

public class ImportDeclarationMetaModel extends NodeMetaModel {
   public PropertyMetaModel isAsteriskPropertyMetaModel;
   public PropertyMetaModel isStaticPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   ImportDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ImportDeclaration.class, "ImportDeclaration", "com.github.javaparser.ast", false, false);
   }
}
