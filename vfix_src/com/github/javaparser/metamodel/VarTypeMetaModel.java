package com.github.javaparser.metamodel;

import com.github.javaparser.ast.type.VarType;
import java.util.Optional;

public class VarTypeMetaModel extends TypeMetaModel {
   VarTypeMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, VarType.class, "VarType", "com.github.javaparser.ast.type", false, false);
   }
}
