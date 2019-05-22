package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.validator.chunks.ModifierValidator;
import com.github.javaparser.ast.validator.chunks.UnderscoreKeywordValidator;

public class Java9Validator extends Java8Validator {
   protected final Validator underscoreKeywordValidator = new UnderscoreKeywordValidator();
   protected final Validator modifiers = new ModifierValidator(true, true, true);
   protected final SingleNodeTypeValidator<TryStmt> tryWithResources = new SingleNodeTypeValidator(TryStmt.class, (n, reporter) -> {
      if (n.getCatchClauses().isEmpty() && n.getResources().isEmpty() && !n.getFinallyBlock().isPresent()) {
         reporter.report((NodeWithTokenRange)n, "Try has no finally, no catch, and no resources.");
      }

   });

   public Java9Validator() {
      this.add(this.underscoreKeywordValidator);
      this.remove(this.noModules);
      this.replace(this.modifiersWithoutPrivateInterfaceMethods, this.modifiers);
      this.replace(this.tryWithLimitedResources, this.tryWithResources);
   }
}
