package com.github.javaparser.metamodel;

import com.github.javaparser.ast.ArrayCreationLevel;
import java.util.Optional;

public class ArrayCreationLevelMetaModel extends NodeMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;
   public PropertyMetaModel dimensionPropertyMetaModel;

   ArrayCreationLevelMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ArrayCreationLevel.class, "ArrayCreationLevel", "com.github.javaparser.ast", false, false);
   }
}
