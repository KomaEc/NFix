package com.github.javaparser.symbolsolver.resolution.typeinference.bounds;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.Bound;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariable;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariableSubstitution;
import java.util.List;
import java.util.Set;

public class CapturesBound extends Bound {
   private List<InferenceVariable> inferenceVariables;
   private List<ResolvedType> typesOrWildcards;

   public CapturesBound(List<InferenceVariable> inferenceVariables, List<ResolvedType> typesOrWildcards) {
      this.inferenceVariables = inferenceVariables;
      this.typesOrWildcards = typesOrWildcards;
   }

   public boolean isSatisfied(InferenceVariableSubstitution inferenceVariableSubstitution) {
      throw new UnsupportedOperationException();
   }

   public Set<InferenceVariable> usedInferenceVariables() {
      throw new UnsupportedOperationException();
   }

   public List<InferenceVariable> getInferenceVariables() {
      return this.inferenceVariables;
   }

   public List<ResolvedType> getTypesOrWildcards() {
      return this.typesOrWildcards;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CapturesBound that = (CapturesBound)o;
         return !this.inferenceVariables.equals(that.inferenceVariables) ? false : this.typesOrWildcards.equals(that.typesOrWildcards);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.inferenceVariables.hashCode();
      result = 31 * result + this.typesOrWildcards.hashCode();
      return result;
   }

   public String toString() {
      return "CapturesBound{inferenceVariables=" + this.inferenceVariables + ", typesOrWildcards=" + this.typesOrWildcards + '}';
   }
}
