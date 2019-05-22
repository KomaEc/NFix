package com.github.javaparser.resolution.types;

public class ResolvedLambdaConstraintType implements ResolvedType {
   private ResolvedType bound;

   private ResolvedLambdaConstraintType(ResolvedType bound) {
      this.bound = bound;
   }

   public String describe() {
      return "? super " + this.bound.describe();
   }

   public ResolvedType getBound() {
      return this.bound;
   }

   public boolean isConstraint() {
      return true;
   }

   public ResolvedLambdaConstraintType asConstraintType() {
      return this;
   }

   public static ResolvedLambdaConstraintType bound(ResolvedType bound) {
      return new ResolvedLambdaConstraintType(bound);
   }

   public boolean isAssignableBy(ResolvedType other) {
      return this.bound.isAssignableBy(other);
   }

   public String toString() {
      return "LambdaConstraintType{bound=" + this.bound + '}';
   }
}
