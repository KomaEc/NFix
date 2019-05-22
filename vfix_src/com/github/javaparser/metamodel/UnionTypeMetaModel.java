package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.UnionType;
import java.util.Optional;

public class UnionTypeMetaModel extends TypeMetaModel {
   public PropertyMetaModel elementsPropertyMetaModel;

   UnionTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, UnionType.class, "UnionType", "com.github.javaparser.ast.type", false, false);
   }
}
