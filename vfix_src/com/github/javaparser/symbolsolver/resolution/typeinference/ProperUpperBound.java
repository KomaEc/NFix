package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.types.ResolvedType;

public class ProperUpperBound {
   private InferenceVariable inferenceVariable;
   private ResolvedType properType;

   public ProperUpperBound(InferenceVariable inferenceVariable, ResolvedType properType) {
      this.inferenceVariable = inferenceVariable;
      this.properType = properType;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ProperUpperBound that = (ProperUpperBound)o;
         return !this.inferenceVariable.equals(that.inferenceVariable) ? false : this.properType.equals(that.properType);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.inferenceVariable.hashCode();
      result = 31 * result + this.properType.hashCode();
      return result;
   }

   public String toString() {
      return "ProperUpperBound{inferenceVariable=" + this.inferenceVariable + ", properType=" + this.properType + '}';
   }

   public InferenceVariable getInferenceVariable() {
      return this.inferenceVariable;
   }

   public ResolvedType getProperType() {
      return this.properType;
   }
}
