package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.VoidType;
import java.util.Optional;

public class VoidTypeMetaModel extends TypeMetaModel {
   VoidTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, VoidType.class, "VoidType", "com.github.javaparser.ast.type", false, false);
   }
}
