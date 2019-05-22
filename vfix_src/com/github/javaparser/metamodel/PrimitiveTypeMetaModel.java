package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.PrimitiveType;
import java.util.Optional;

public class PrimitiveTypeMetaModel extends TypeMetaModel {
   public PropertyMetaModel typePropertyMetaModel;

   PrimitiveTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, PrimitiveType.class, "PrimitiveType", "com.github.javaparser.ast.type", false, false);
   }
}
