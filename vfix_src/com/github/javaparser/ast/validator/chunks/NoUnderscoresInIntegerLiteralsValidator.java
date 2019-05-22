package com.github.javaparser.ast.validator.chunks;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.validator.ProblemReporter;
import com.github.javaparser.ast.validator.VisitorValidator;

public class NoUnderscoresInIntegerLiteralsValidator extends VisitorValidator {
   public void visit(IntegerLiteralExpr n, ProblemReporter arg) {
      validate(n, arg);
      super.visit(n, arg);
   }

   public void visit(LongLiteralExpr n, ProblemReporter arg) {
      validate(n, arg);
      super.visit(n, arg);
   }

   private static void validate(LiteralStringValueExpr n, ProblemReporter arg) {
      if (n.getValue().contains("_")) {
         arg.report((NodeWithTokenRange)n, "Underscores in literal values are not supported.");
      }

   }
}
