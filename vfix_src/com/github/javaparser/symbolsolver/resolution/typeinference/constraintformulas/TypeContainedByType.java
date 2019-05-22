package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;

public class TypeContainedByType extends ConstraintFormula {
   private ResolvedType S;
   private ResolvedType T;

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      if (TypeHelper.isProperType(this.T) && !this.T.isWildcard()) {
         throw new UnsupportedOperationException();
      } else if (this.T.isWildcard() && !this.T.asWildcard().isBounded()) {
         return ConstraintFormula.ReductionResult.trueResult();
      } else if (this.T.isWildcard() && this.T.asWildcard().isExtends()) {
         throw new UnsupportedOperationException();
      } else if (this.T.isWildcard() && this.T.asWildcard().isSuper()) {
         throw new UnsupportedOperationException();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TypeContainedByType that = (TypeContainedByType)o;
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
      return "TypeContainedByType{S=" + this.S + ", T=" + this.T + '}';
   }
}
