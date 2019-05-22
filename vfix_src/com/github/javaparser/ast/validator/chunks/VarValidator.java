package com.github.javaparser.ast.validator.chunks;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.validator.ProblemReporter;
import com.github.javaparser.ast.validator.TypedValidator;
import java.util.Optional;

public class VarValidator implements TypedValidator<VarType> {
   private boolean varAllowedInLambdaParameters;

   public VarValidator(boolean varAllowedInLambdaParameters) {
      this.varAllowedInLambdaParameters = varAllowedInLambdaParameters;
   }

   public void accept(VarType node, ProblemReporter reporter) {
      Optional<VariableDeclarator> variableDeclarator = node.findAncestor(VariableDeclarator.class);
      if (!variableDeclarator.isPresent()) {
         if (this.varAllowedInLambdaParameters) {
            boolean valid = (Boolean)node.findAncestor(Parameter.class).flatMap(Node::getParentNode).map((p) -> {
               return p instanceof LambdaExpr;
            }).orElse(false);
            if (valid) {
               return;
            }
         }

         this.reportIllegalPosition(node, reporter);
      } else {
         variableDeclarator.ifPresent((vd) -> {
            if (vd.getType().isArrayType()) {
               reporter.report((NodeWithTokenRange)vd, "\"var\" cannot have extra array brackets.");
            }

            Optional<Node> variableDeclarationExpr = vd.getParentNode();
            if (!variableDeclarationExpr.isPresent()) {
               this.reportIllegalPosition(node, reporter);
            } else {
               variableDeclarationExpr.ifPresent((vdeNode) -> {
                  if (!(vdeNode instanceof VariableDeclarationExpr)) {
                     this.reportIllegalPosition(node, reporter);
                  } else {
                     VariableDeclarationExpr vde = (VariableDeclarationExpr)vdeNode;
                     if (vde.getVariables().size() > 1) {
                        reporter.report((NodeWithTokenRange)vde, "\"var\" only takes a single variable.");
                     }

                     Optional<Node> container = vdeNode.getParentNode();
                     if (!container.isPresent()) {
                        this.reportIllegalPosition(node, reporter);
                     } else {
                        container.ifPresent((c) -> {
                           boolean positionIsFine = c instanceof ForStmt || c instanceof ForeachStmt || c instanceof ExpressionStmt;
                           if (!positionIsFine) {
                              this.reportIllegalPosition(node, reporter);
                           }

                           if (c instanceof ExpressionStmt) {
                              if (!vd.getInitializer().isPresent()) {
                                 reporter.report((NodeWithTokenRange)node, "\"var\" needs an initializer.");
                              }

                              vd.getInitializer().ifPresent((initializer) -> {
                                 if (initializer instanceof NullLiteralExpr) {
                                    reporter.report((NodeWithTokenRange)node, "\"var\" cannot infer type from just null.");
                                 }

                                 if (initializer instanceof ArrayInitializerExpr) {
                                    reporter.report((NodeWithTokenRange)node, "\"var\" cannot infer array types.");
                                 }

                              });
                           }

                        });
                     }
                  }
               });
            }
         });
      }
   }

   private void reportIllegalPosition(VarType n, ProblemReporter reporter) {
      reporter.report((NodeWithTokenRange)n, "\"var\" is not allowed here.");
   }
}
