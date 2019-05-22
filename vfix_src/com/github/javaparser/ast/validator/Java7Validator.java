package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.UnionType;
import java.util.Iterator;

public class Java7Validator extends Java6Validator {
   protected final SingleNodeTypeValidator<TryStmt> tryWithLimitedResources = new SingleNodeTypeValidator(TryStmt.class, (n, reporter) -> {
      if (n.getCatchClauses().isEmpty() && n.getResources().isEmpty() && !n.getFinallyBlock().isPresent()) {
         reporter.report((NodeWithTokenRange)n, "Try has no finally, no catch, and no resources.");
      }

      Iterator var2 = n.getResources().iterator();

      while(var2.hasNext()) {
         Expression resource = (Expression)var2.next();
         if (!resource.isVariableDeclarationExpr()) {
            reporter.report((NodeWithTokenRange)n, "Try with resources only supports variable declarations.");
         }
      }

   });
   protected final SingleNodeTypeValidator<UnionType> multiCatch = new SingleNodeTypeValidator(UnionType.class, (n, reporter) -> {
      if (n.getElements().size() == 1) {
         reporter.report((NodeWithTokenRange)n, "Union type (multi catch) must have at least two elements.");
      }

   });

   public Java7Validator() {
      this.remove(this.genericsWithoutDiamondOperator);
      this.replace(this.tryWithoutResources, this.tryWithLimitedResources);
      this.remove(this.noStringsInSwitch);
      this.remove(this.noBinaryIntegerLiterals);
      this.remove(this.noUnderscoresInIntegerLiterals);
      this.replace(this.noMultiCatch, this.multiCatch);
   }
}
