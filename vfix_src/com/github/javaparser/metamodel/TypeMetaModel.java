package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.Type;
import java.util.Optional;

public class TypeMetaModel extends NodeMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;

   TypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, Type.class, "Type", "com.github.javaparser.ast.type", true, false);
   }

   protected TypeMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
