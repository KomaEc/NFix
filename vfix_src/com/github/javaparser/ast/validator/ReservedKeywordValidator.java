package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.utils.CodeGenerationUtils;

public class ReservedKeywordValidator extends VisitorValidator {
   private final String keyword;
   private final String error;

   public ReservedKeywordValidator(String keyword) {
      this.keyword = keyword;
      this.error = CodeGenerationUtils.f("'%s' cannot be used as an identifier as it is a keyword.", keyword);
   }

   public void visit(Name n, ProblemReporter arg) {
      if (n.getIdentifier().equals(this.keyword)) {
         arg.report((NodeWithTokenRange)n, this.error);
      }

      super.visit(n, arg);
   }

   public void visit(SimpleName n, ProblemReporter arg) {
      if (n.getIdentifier().equals(this.keyword)) {
         arg.report((NodeWithTokenRange)n, this.error);
      }

      super.visit(n, arg);
   }
}
