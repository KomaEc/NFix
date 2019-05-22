package com.github.javaparser.metamodel;

import com.github.javaparser.ast.modules.ModuleDeclaration;
import java.util.Optional;

public class ModuleDeclarationMetaModel extends NodeMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;
   public PropertyMetaModel isOpenPropertyMetaModel;
   public PropertyMetaModel moduleStmtsPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   ModuleDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ModuleDeclaration.class, "ModuleDeclaration", "com.github.javaparser.ast.modules", false, false);
   }
}
