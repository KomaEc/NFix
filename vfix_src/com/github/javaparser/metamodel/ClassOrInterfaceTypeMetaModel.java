package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.Optional;

public class ClassOrInterfaceTypeMetaModel extends ReferenceTypeMetaModel {
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel scopePropertyMetaModel;
   public PropertyMetaModel typeArgumentsPropertyMetaModel;
   public PropertyMetaModel usingDiamondOperatorPropertyMetaModel;

   ClassOrInterfaceTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ClassOrInterfaceType.class, "ClassOrInterfaceType", "com.github.javaparser.ast.type", false, false);
   }
}
