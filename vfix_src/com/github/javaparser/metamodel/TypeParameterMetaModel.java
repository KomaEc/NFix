package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.TypeParameter;
import java.util.Optional;

public class TypeParameterMetaModel extends ReferenceTypeMetaModel {
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel typeBoundPropertyMetaModel;

   TypeParameterMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, TypeParameter.class, "TypeParameter", "com.github.javaparser.ast.type", false, false);
   }
}
