package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.resolution.types.ResolvedIntersectionType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.NullType;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.SubtypeOfBound;

public class TypeSubtypeOfType extends ConstraintFormula {
   private ResolvedType S;
   private ResolvedType T;
   private TypeSolver typeSolver;

   public TypeSubtypeOfType(TypeSolver typeSolver, ResolvedType S, ResolvedType T) {
      this.typeSolver = typeSolver;
      this.S = S;
      this.T = T;
   }

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      if (TypeHelper.isProperType(this.S) && TypeHelper.isProperType(this.T)) {
         return this.T.isAssignableBy(this.S) ? ConstraintFormula.ReductionResult.trueResult() : ConstraintFormula.ReductionResult.falseResult();
      } else if (this.S instanceof NullType) {
         return ConstraintFormula.ReductionResult.trueResult();
      } else if (this.T instanceof NullType) {
         return ConstraintFormula.ReductionResult.falseResult();
      } else if (TypeHelper.isInferenceVariable(this.S)) {
         return ConstraintFormula.ReductionResult.oneBound(new SubtypeOfBound(this.S, this.T));
      } else if (TypeHelper.isInferenceVariable(this.T)) {
         return ConstraintFormula.ReductionResult.oneBound(new SubtypeOfBound(this.S, this.T));
      } else if (this.T.isTypeVariable()) {
         if (this.S instanceof ResolvedIntersectionType) {
            throw new UnsupportedOperationException();
         } else {
            return this.T.asTypeVariable().asTypeParameter().hasLowerBound() ? ConstraintFormula.ReductionResult.oneConstraint(new TypeSubtypeOfType(this.typeSolver, this.S, this.T.asTypeVariable().asTypeParameter().getLowerBound())) : ConstraintFormula.ReductionResult.falseResult();
         }
      } else {
         throw new UnsupportedOperationException("S = " + this.S + ", T = " + this.T);
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TypeSubtypeOfType that = (TypeSubtypeOfType)o;
         return !this.S.equals(that.S) ? false : this.T.equals(that.T);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.S.hashCode();
      result = 31 * result + this.T.hashCode();
      return result;
   }

   public String toString() {
      return "TypeSubtypeOfType{S=" + this.S + ", T=" + this.T + '}';
   }
}
