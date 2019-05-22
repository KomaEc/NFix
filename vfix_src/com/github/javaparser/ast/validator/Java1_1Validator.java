package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;

public class Java1_1Validator extends Java1_0Validator {
   protected final Validator innerClasses = new SingleNodeTypeValidator(ClassOrInterfaceDeclaration.class, (n, reporter) -> {
      n.getParentNode().ifPresent((p) -> {
         if (p instanceof LocalClassDeclarationStmt && n.isInterface()) {
            reporter.report((NodeWithTokenRange)n, "There is no such thing as a local interface.");
         }

      });
   });

   public Java1_1Validator() {
      this.replace(this.noInnerClasses, this.innerClasses);
      this.remove(this.noReflection);
   }
}
