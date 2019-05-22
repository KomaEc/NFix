package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.validator.chunks.VarValidator;

public class Java11Validator extends Java10Validator {
   protected final Validator varAlsoInLambdaParameters = new SingleNodeTypeValidator(VarType.class, new VarValidator(true));

   public Java11Validator() {
      this.replace(this.varOnlyOnLocalVariableDefinitionAndFor, this.varAlsoInLambdaParameters);
   }
}
