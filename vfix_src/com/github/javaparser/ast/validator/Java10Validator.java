package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.validator.chunks.VarValidator;

public class Java10Validator extends Java9Validator {
   protected final Validator varOnlyOnLocalVariableDefinitionAndFor = new SingleNodeTypeValidator(VarType.class, new VarValidator(false));

   public Java10Validator() {
      this.add(this.varOnlyOnLocalVariableDefinitionAndFor);
   }
}
