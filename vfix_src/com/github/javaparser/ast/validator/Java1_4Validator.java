package com.github.javaparser.ast.validator;

public class Java1_4Validator extends Java1_3Validator {
   public Java1_4Validator() {
      this.remove(this.noAssertKeyword);
   }
}
