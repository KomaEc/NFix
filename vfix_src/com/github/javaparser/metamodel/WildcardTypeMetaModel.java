package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.WildcardType;
import java.util.Optional;

public class WildcardTypeMetaModel extends TypeMetaModel {
   public PropertyMetaModel extendedTypePropertyMetaModel;
   public PropertyMetaModel superTypePropertyMetaModel;

   WildcardTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, WildcardType.class, "WildcardType", "com.github.javaparser.ast.type", false, false);
   }
}
