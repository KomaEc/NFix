package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.logic.FunctionalInterfaceLogic;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;
import com.github.javaparser.symbolsolver.resolution.typeinference.ControlFlowLogic;
import com.github.javaparser.symbolsolver.resolution.typeinference.ExpressionHelper;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariable;
import com.github.javaparser.symbolsolver.resolution.typeinference.MethodType;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeInference;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeInferenceCache;
import com.github.javaparser.utils.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpressionCompatibleWithType extends ConstraintFormula {
   private TypeSolver typeSolver;
   private Expression expression;
   private ResolvedType T;

   public ExpressionCompatibleWithType(TypeSolver typeSolver, Expression expression, ResolvedType T) {
      this.typeSolver = typeSolver;
      this.expression = expression;
      this.T = T;
   }

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      if (TypeHelper.isProperType(this.T)) {
         return TypeHelper.isCompatibleInALooseInvocationContext(this.typeSolver, this.expression, this.T) ? ConstraintFormula.ReductionResult.trueResult() : ConstraintFormula.ReductionResult.falseResult();
      } else if (ExpressionHelper.isStandaloneExpression(this.expression)) {
         ResolvedType s = JavaParserFacade.get(this.typeSolver).getType(this.expression, false);
         return ConstraintFormula.ReductionResult.empty().withConstraint(new TypeCompatibleWithType(this.typeSolver, s, this.T));
      } else if (ExpressionHelper.isPolyExpression(this.expression)) {
         if (this.expression instanceof EnclosedExpr) {
            EnclosedExpr enclosedExpr = (EnclosedExpr)this.expression;
            return ConstraintFormula.ReductionResult.oneConstraint(new ExpressionCompatibleWithType(this.typeSolver, enclosedExpr.getInner(), this.T));
         } else if (this.expression instanceof ObjectCreationExpr) {
            BoundSet B3 = (new TypeInference(this.typeSolver)).invocationTypeInferenceBoundsSetB3();
            return ConstraintFormula.ReductionResult.bounds(B3);
         } else if (this.expression instanceof MethodCallExpr) {
            throw new UnsupportedOperationException();
         } else if (this.expression instanceof ConditionalExpr) {
            ConditionalExpr conditionalExpr = (ConditionalExpr)this.expression;
            return ConstraintFormula.ReductionResult.withConstraints(new ExpressionCompatibleWithType(this.typeSolver, conditionalExpr.getThenExpr(), this.T), new ExpressionCompatibleWithType(this.typeSolver, conditionalExpr.getElseExpr(), this.T));
         } else if (!(this.expression instanceof LambdaExpr)) {
            if (this.expression instanceof MethodReferenceExpr) {
               throw new UnsupportedOperationException();
            } else {
               throw new RuntimeException("This should not happen");
            }
         } else {
            LambdaExpr lambdaExpr = (LambdaExpr)this.expression;
            if (!FunctionalInterfaceLogic.isFunctionalInterfaceType(this.T)) {
               return ConstraintFormula.ReductionResult.falseResult();
            } else {
               Pair<ResolvedType, Boolean> result = TypeHelper.groundTargetTypeOfLambda(lambdaExpr, this.T, this.typeSolver);
               ResolvedType TFirst = (ResolvedType)result.a;
               MethodType targetFunctionType = TypeHelper.getFunctionType(TFirst);
               targetFunctionType = this.replaceTypeVariablesWithInferenceVariables(targetFunctionType);
               if ((Boolean)result.b) {
                  throw new UnsupportedOperationException();
               } else if (targetFunctionType.getFormalArgumentTypes().size() != lambdaExpr.getParameters().size()) {
                  return ConstraintFormula.ReductionResult.falseResult();
               } else if (targetFunctionType.getReturnType().isVoid()) {
                  throw new UnsupportedOperationException();
               } else if (!targetFunctionType.getReturnType().isVoid() && lambdaExpr.getBody() instanceof BlockStmt && !this.isValueCompatibleBlock(lambdaExpr.getBody())) {
                  return ConstraintFormula.ReductionResult.falseResult();
               } else {
                  List<ConstraintFormula> constraints = new LinkedList();
                  boolean hasExplicitlyDeclaredTypes = lambdaExpr.getParameters().stream().anyMatch((p) -> {
                     return !(p.getType() instanceof UnknownType);
                  });
                  if (hasExplicitlyDeclaredTypes) {
                     throw new UnsupportedOperationException();
                  } else {
                     if (!targetFunctionType.getReturnType().isVoid()) {
                        ResolvedType R = targetFunctionType.getReturnType();
                        Expression e;
                        if (TypeHelper.isProperType(R)) {
                           if (lambdaExpr.getBody() instanceof BlockStmt) {
                              List<Expression> resultExpressions = ExpressionHelper.getResultExpressions((BlockStmt)lambdaExpr.getBody());
                              Iterator var10 = resultExpressions.iterator();

                              while(var10.hasNext()) {
                                 Expression e = (Expression)var10.next();
                                 if (!ExpressionHelper.isCompatibleInAssignmentContext(e, R, this.typeSolver)) {
                                    return ConstraintFormula.ReductionResult.falseResult();
                                 }
                              }
                           } else {
                              e = ((ExpressionStmt)lambdaExpr.getBody()).getExpression();
                              if (!ExpressionHelper.isCompatibleInAssignmentContext(e, R, this.typeSolver)) {
                                 return ConstraintFormula.ReductionResult.falseResult();
                              }
                           }
                        } else if (lambdaExpr.getBody() instanceof BlockStmt) {
                           this.getAllReturnExpressions((BlockStmt)lambdaExpr.getBody()).forEach((ex) -> {
                              constraints.add(new ExpressionCompatibleWithType(this.typeSolver, ex, R));
                           });
                        } else {
                           for(int i = 0; i < lambdaExpr.getParameters().size(); ++i) {
                              ResolvedType paramType = (ResolvedType)targetFunctionType.getFormalArgumentTypes().get(i);
                              TypeInferenceCache.record(this.typeSolver, lambdaExpr, lambdaExpr.getParameter(i).getNameAsString(), paramType);
                           }

                           e = ((ExpressionStmt)lambdaExpr.getBody()).getExpression();
                           constraints.add(new ExpressionCompatibleWithType(this.typeSolver, e, R));
                        }
                     }

                     return ConstraintFormula.ReductionResult.withConstraints((List)constraints);
                  }
               }
            }
         }
      } else {
         throw new RuntimeException("This should not happen");
      }
   }

   private List<Expression> getAllReturnExpressions(BlockStmt blockStmt) {
      return (List)blockStmt.findAll(ReturnStmt.class).stream().filter((r) -> {
         return r.getExpression().isPresent();
      }).map((r) -> {
         return (Expression)r.getExpression().get();
      }).collect(Collectors.toList());
   }

   private boolean isValueCompatibleBlock(Statement statement) {
      if (statement instanceof BlockStmt) {
         if (!ControlFlowLogic.getInstance().canCompleteNormally(statement)) {
            return true;
         } else {
            List<ReturnStmt> returnStmts = statement.findAll(ReturnStmt.class);
            return returnStmts.stream().allMatch((r) -> {
               return r.getExpression().isPresent();
            });
         }
      } else {
         return false;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ExpressionCompatibleWithType that = (ExpressionCompatibleWithType)o;
         if (!this.typeSolver.equals(that.typeSolver)) {
            return false;
         } else {
            return !this.expression.equals(that.expression) ? false : this.T.equals(that.T);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.typeSolver.hashCode();
      result = 31 * result + this.expression.hashCode();
      result = 31 * result + this.T.hashCode();
      return result;
   }

   public String toString() {
      return "ExpressionCompatibleWithType{typeSolver=" + this.typeSolver + ", expression=" + this.expression + ", T=" + this.T + '}';
   }

   private MethodType replaceTypeVariablesWithInferenceVariables(MethodType methodType) {
      Map<ResolvedTypeVariable, InferenceVariable> correspondences = new HashMap();
      List<ResolvedType> newFormalArgumentTypes = new LinkedList();
      Iterator var4 = methodType.getFormalArgumentTypes().iterator();

      while(var4.hasNext()) {
         ResolvedType formalArg = (ResolvedType)var4.next();
         newFormalArgumentTypes.add(this.replaceTypeVariablesWithInferenceVariables(formalArg, correspondences));
      }

      ResolvedType newReturnType = this.replaceTypeVariablesWithInferenceVariables(methodType.getReturnType(), correspondences);
      return new MethodType(methodType.getTypeParameters(), newFormalArgumentTypes, newReturnType, methodType.getExceptionTypes());
   }

   private ResolvedType replaceTypeVariablesWithInferenceVariables(ResolvedType originalType, Map<ResolvedTypeVariable, InferenceVariable> correspondences) {
      if (originalType.isTypeVariable()) {
         if (!correspondences.containsKey(originalType.asTypeVariable())) {
            correspondences.put(originalType.asTypeVariable(), InferenceVariable.unnamed(originalType.asTypeVariable().asTypeParameter()));
         }

         return (ResolvedType)correspondences.get(originalType.asTypeVariable());
      } else if (originalType.isPrimitive()) {
         return originalType;
      } else {
         throw new UnsupportedOperationException(originalType.toString());
      }
   }
}
