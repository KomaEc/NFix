package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.IntersectionType;
import java.util.Optional;

public class IntersectionTypeMetaModel extends TypeMetaModel {
   public PropertyMetaModel elementsPropertyMetaModel;

   IntersectionTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, IntersectionType.class, "IntersectionType", "com.github.javaparser.ast.type", false, false);
   }
}
