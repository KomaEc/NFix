package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.Optional;

public class TypeDeclarationMetaModel extends BodyDeclarationMetaModel {
   public PropertyMetaModel membersPropertyMetaModel;
   public PropertyMetaModel modifiersPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;

   TypeDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, TypeDeclaration.class, "TypeDeclaration", "com.github.javaparser.ast.body", true, true);
   }

   protected TypeDeclarationMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
