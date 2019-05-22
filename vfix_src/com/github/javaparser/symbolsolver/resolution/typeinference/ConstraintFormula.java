package com.github.javaparser.symbolsolver.resolution.typeinference;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class ConstraintFormula {
   public abstract ConstraintFormula.ReductionResult reduce(BoundSet var1);

   public static class ReductionResult {
      private BoundSet boundSet = BoundSet.empty();
      private List<ConstraintFormula> constraintFormulas = new LinkedList();

      public BoundSet getBoundSet() {
         return this.boundSet;
      }

      public List<ConstraintFormula> getConstraintFormulas() {
         return this.constraintFormulas;
      }

      public static ConstraintFormula.ReductionResult empty() {
         return new ConstraintFormula.ReductionResult();
      }

      public ConstraintFormula.ReductionResult withConstraint(ConstraintFormula constraintFormula) {
         ConstraintFormula.ReductionResult newInstance = new ConstraintFormula.ReductionResult();
         newInstance.boundSet = this.boundSet;
         newInstance.constraintFormulas = new LinkedList();
         newInstance.constraintFormulas.addAll(this.constraintFormulas);
         newInstance.constraintFormulas.add(constraintFormula);
         return newInstance;
      }

      public ConstraintFormula.ReductionResult withBound(Bound bound) {
         ConstraintFormula.ReductionResult newInstance = new ConstraintFormula.ReductionResult();
         newInstance.boundSet = this.boundSet.withBound(bound);
         newInstance.constraintFormulas = this.constraintFormulas;
         return newInstance;
      }

      private ReductionResult() {
      }

      public static ConstraintFormula.ReductionResult trueResult() {
         return empty();
      }

      public static ConstraintFormula.ReductionResult falseResult() {
         return empty().withBound(Bound.falseBound());
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            ConstraintFormula.ReductionResult that = (ConstraintFormula.ReductionResult)o;
            return !this.boundSet.equals(that.boundSet) ? false : this.constraintFormulas.equals(that.constraintFormulas);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.boundSet.hashCode();
         result = 31 * result + this.constraintFormulas.hashCode();
         return result;
      }

      public String toString() {
         return "ReductionResult{boundSet=" + this.boundSet + ", constraintFormulas=" + this.constraintFormulas + '}';
      }

      public ConstraintFormula getConstraint(int index) {
         if (this.constraintFormulas.size() <= index) {
            throw new IllegalArgumentException("Constraint with index " + index + " is not available as there are " + this.constraintFormulas.size() + " constraints");
         } else {
            return (ConstraintFormula)this.constraintFormulas.get(index);
         }
      }

      public static ConstraintFormula.ReductionResult oneConstraint(ConstraintFormula constraintFormula) {
         return empty().withConstraint(constraintFormula);
      }

      public static ConstraintFormula.ReductionResult withConstraints(ConstraintFormula... constraints) {
         return withConstraints(Arrays.asList(constraints));
      }

      public static ConstraintFormula.ReductionResult oneBound(Bound bound) {
         return empty().withBound(bound);
      }

      public static ConstraintFormula.ReductionResult withConstraints(List<ConstraintFormula> constraints) {
         ConstraintFormula.ReductionResult reductionResult = new ConstraintFormula.ReductionResult();
         reductionResult.constraintFormulas.addAll(constraints);
         return reductionResult;
      }

      public static ConstraintFormula.ReductionResult bounds(BoundSet bounds) {
         ConstraintFormula.ReductionResult reductionResult = new ConstraintFormula.ReductionResult();
         reductionResult.boundSet = bounds;
         return reductionResult;
      }
   }
}
