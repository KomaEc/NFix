package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.validator.chunks.ModifierValidator;

public class Java8Validator extends Java7Validator {
   protected final Validator modifiersWithoutPrivateInterfaceMethods = new ModifierValidator(true, true, false);
   protected final Validator defaultMethodsInInterface = new SingleNodeTypeValidator(ClassOrInterfaceDeclaration.class, (n, reporter) -> {
      if (n.isInterface()) {
         n.getMethods().forEach((m) -> {
            if (m.isDefault() && !m.getBody().isPresent()) {
               reporter.report((NodeWithTokenRange)m, "'default' methods must have a body.");
            }

         });
      }

   });

   public Java8Validator() {
      this.replace(this.modifiersWithoutDefaultAndStaticInterfaceMethodsAndPrivateInterfaceMethods, this.modifiersWithoutPrivateInterfaceMethods);
      this.add(this.defaultMethodsInInterface);
      this.remove(this.noLambdas);
   }
}
