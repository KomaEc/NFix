package com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typeinference.BoundSet;
import com.github.javaparser.symbolsolver.resolution.typeinference.ConstraintFormula;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class TypeCompatibleWithType extends ConstraintFormula {
   private ResolvedType s;
   private ResolvedType t;
   private TypeSolver typeSolver;

   public TypeCompatibleWithType(TypeSolver typeSolver, ResolvedType s, ResolvedType t) {
      this.typeSolver = typeSolver;
      this.s = s;
      this.t = t;
   }

   public ConstraintFormula.ReductionResult reduce(BoundSet currentBoundSet) {
      if (TypeHelper.isProperType(this.s) && TypeHelper.isProperType(this.t)) {
         return TypeHelper.isCompatibleInALooseInvocationContext(this.s, this.t) ? ConstraintFormula.ReductionResult.trueResult() : ConstraintFormula.ReductionResult.falseResult();
      } else {
         ReflectionTypeSolver typeSolver;
         ReferenceTypeImpl tFirst;
         if (this.s.isPrimitive()) {
            typeSolver = new ReflectionTypeSolver();
            tFirst = new ReferenceTypeImpl(typeSolver.solveType(this.s.asPrimitive().getBoxTypeQName()), typeSolver);
            return ConstraintFormula.ReductionResult.oneConstraint(new TypeCompatibleWithType(typeSolver, tFirst, this.t));
         } else if (this.t.isPrimitive()) {
            typeSolver = new ReflectionTypeSolver();
            tFirst = new ReferenceTypeImpl(typeSolver.solveType(this.t.asPrimitive().getBoxTypeQName()), typeSolver);
            return ConstraintFormula.ReductionResult.oneConstraint(new TypeSameAsType(this.s, tFirst));
         } else {
            if (this.t.isReferenceType() && !this.t.asReferenceType().getTypeDeclaration().getTypeParameters().isEmpty()) {
               boolean condition1 = this.t.isAssignableBy(this.s);
               ResolvedType G = this.t.asReferenceType().toRawType();
               boolean condition2 = G.isAssignableBy(this.s);
               if (!condition1 && condition2) {
                  return ConstraintFormula.ReductionResult.trueResult();
               }
            }

            if (this.t.isArray()) {
               throw new UnsupportedOperationException();
            } else {
               return ConstraintFormula.ReductionResult.empty().withConstraint(new TypeSubtypeOfType(this.typeSolver, this.s, this.t));
            }
         }
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TypeCompatibleWithType that = (TypeCompatibleWithType)o;
         return !this.s.equals(that.s) ? false : this.t.equals(that.t);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.s.hashCode();
      result = 31 * result + this.t.hashCode();
      return result;
   }

   public String toString() {
      return "TypeCompatibleWithType{s=" + this.s + ", t=" + this.t + '}';
   }
}
