package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.ArrayType;
import java.util.Optional;

public class ArrayTypeMetaModel extends ReferenceTypeMetaModel {
   public PropertyMetaModel componentTypePropertyMetaModel;
   public PropertyMetaModel originPropertyMetaModel;

   ArrayTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ArrayType.class, "ArrayType", "com.github.javaparser.ast.type", false, false);
   }
}
