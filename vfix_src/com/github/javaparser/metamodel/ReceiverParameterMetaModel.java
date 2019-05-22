package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.ReceiverParameter;
import java.util.Optional;

public class ReceiverParameterMetaModel extends NodeMetaModel {
   public PropertyMetaModel annotationsPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   ReceiverParameterMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, ReceiverParameter.class, "ReceiverParameter", "com.github.javaparser.ast.body", false, false);
   }
}
