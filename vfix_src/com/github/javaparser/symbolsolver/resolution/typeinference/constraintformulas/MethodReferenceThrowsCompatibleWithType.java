package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;

public class MethodReferenceThrowsCompatibleWithType extends ConstraintFormula {
   private MethodReferenceExpr methodReference;
   private ResolvedType T;

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MethodReferenceThrowsCompatibleWithType that = (MethodReferenceThrowsCompatibleWithType)o;
         return !this.methodReference.equals(that.methodReference) ? false : this.T.equals(that.T);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.methodReference.hashCode();
      result = 31 * result + this.T.hashCode();
      return result;
   }

   public String toString() {
      return "MethodReferenceThrowsCompatibleWithType{methodReference=" + this.methodReference + ", T=" + this.T + '}';
   }
}
