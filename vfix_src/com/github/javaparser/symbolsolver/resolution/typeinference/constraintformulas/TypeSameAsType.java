package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.SameAsBound;
import java.util.List;

public class TypeSameAsType extends ConstraintFormula {
   private ResolvedType S;
   private ResolvedType T;

   public TypeSameAsType(ResolvedType s, ResolvedType t) {
      this.S = s;
      this.T = t;
   }

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      if (!this.S.isWildcard() && !this.T.isWildcard()) {
         if (TypeHelper.isProperType(this.S) && TypeHelper.isProperType(this.T)) {
            return this.S.equals(this.T) ? ConstraintFormula.ReductionResult.trueResult() : ConstraintFormula.ReductionResult.falseResult();
         } else if (!this.S.isNull() && !this.T.isNull()) {
            if (TypeHelper.isInferenceVariable(this.S) && !this.T.isPrimitive()) {
               return ConstraintFormula.ReductionResult.oneBound(new SameAsBound(this.S, this.T));
            } else if (TypeHelper.isInferenceVariable(this.T) && !this.S.isPrimitive()) {
               return ConstraintFormula.ReductionResult.oneBound(new SameAsBound(this.S, this.T));
            } else if (this.S.isReferenceType() && this.T.isReferenceType() && this.S.asReferenceType().toRawType().equals(this.T.asReferenceType().toRawType())) {
               ConstraintFormula.ReductionResult res = ConstraintFormula.ReductionResult.empty();
               List<ResolvedType> Bs = this.S.asReferenceType().typeParametersValues();
               List<ResolvedType> As = this.T.asReferenceType().typeParametersValues();

               for(int i = 0; i < Bs.size(); ++i) {
                  res = res.withConstraint(new TypeSameAsType((ResolvedType)Bs.get(i), (ResolvedType)As.get(i)));
               }

               return res;
            } else {
               return this.S.isArray() && this.T.isArray() ? ConstraintFormula.ReductionResult.oneConstraint(new TypeSameAsType(this.S.asArrayType().getComponentType(), this.T.asArrayType().getComponentType())) : ConstraintFormula.ReductionResult.falseResult();
            }
         } else {
            return ConstraintFormula.ReductionResult.falseResult();
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TypeSameAsType that = (TypeSameAsType)o;
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
      return "TypeSameAsType{S=" + this.S + ", T=" + this.T + '}';
   }
}
