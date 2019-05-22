package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;

public class LambdaThrowsCompatibleWithType extends ConstraintFormula {
   private LambdaExpr lambdaExpression;
   private ResolvedType T;

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LambdaThrowsCompatibleWithType that = (LambdaThrowsCompatibleWithType)o;
         return !this.lambdaExpression.equals(that.lambdaExpression) ? false : this.T.equals(that.T);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.lambdaExpression.hashCode();
      result = 31 * result + this.T.hashCode();
      return result;
   }

   public String toString() {
      return "LambdaThrowsCompatibleWithType{lambdaExpression=" + this.lambdaExpression + ", T=" + this.T + '}';
   }
}
