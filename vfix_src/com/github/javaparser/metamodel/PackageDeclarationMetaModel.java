package com.github.javaparser.metamodel;

import com.github.javaparser.ast.PackageDeclaration;
import java.util.Optional;

public class PackageDeclarationMetaModel extends NodeMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   PackageDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, PackageDeclaration.class, "PackageDeclaration", "com.github.javaparser.ast", false, false);
   }
}
