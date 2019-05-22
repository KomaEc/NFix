package com.github.javaparser.symbolsolver.resolution.typeinference.bounds;

import com.github.javaparser.symbolsolver.resolution.typeinference.Bound;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariable;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariableSubstitution;
import java.util.HashSet;
import java.util.Set;

public class ThrowsBound extends Bound {
   private InferenceVariable inferenceVariable;

   public ThrowsBound(InferenceVariable inferenceVariable) {
      this.inferenceVariable = inferenceVariable;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ThrowsBound that = (ThrowsBound)o;
         return this.inferenceVariable.equals(that.inferenceVariable);
      } else {
         return false;
      }
   }

   public String toString() {
      return "ThrowsBound{inferenceVariable=" + this.inferenceVariable + '}';
   }

   public int hashCode() {
      return this.inferenceVariable.hashCode();
   }

   public Set<InferenceVariable> usedInferenceVariables() {
      Set<InferenceVariable> variables = new HashSet();
      variables.add(this.inferenceVariable);
      return variables;
   }

   public boolean isSatisfied(InferenceVariableSubstitution inferenceVariableSubstitution) {
      throw new UnsupportedOperationException();
   }

   public boolean isThrowsBoundOn(InferenceVariable inferenceVariable) {
      return inferenceVariable.equals(this.inferenceVariable);
   }
}
