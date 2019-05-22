package com.github.javaparser.symbolsolver.resolution.typeinference.bounds;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.resolution.typeinference.Bound;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariable;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariableSubstitution;
import com.github.javaparser.symbolsolver.resolution.typeinference.Instantiation;
import com.github.javaparser.symbolsolver.resolution.typeinference.TypeHelper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SameAsBound extends Bound {
   private ResolvedType s;
   private ResolvedType t;

   public SameAsBound(ResolvedType s, ResolvedType t) {
      if (!TypeHelper.isInferenceVariable(s) && !TypeHelper.isInferenceVariable(t)) {
         throw new IllegalArgumentException("One of S or T should be an inference variable");
      } else {
         this.s = s;
         this.t = t;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SameAsBound that = (SameAsBound)o;
         return !this.s.equals(that.s) ? false : this.t.equals(that.t);
      } else {
         return false;
      }
   }

   public String toString() {
      return "SameAsBound{s=" + this.s + ", t=" + this.t + '}';
   }

   public int hashCode() {
      int result = this.s.hashCode();
      result = 31 * result + this.t.hashCode();
      return result;
   }

   public Set<InferenceVariable> usedInferenceVariables() {
      Set<InferenceVariable> variables = new HashSet();
      variables.addAll(TypeHelper.usedInferenceVariables(this.s));
      variables.addAll(TypeHelper.usedInferenceVariables(this.t));
      return variables;
   }

   public ResolvedType getS() {
      return this.s;
   }

   public ResolvedType getT() {
      return this.t;
   }

   public boolean isADependency() {
      return !this.isAnInstantiation().isPresent();
   }

   public Optional<Instantiation> isAnInstantiation() {
      if (TypeHelper.isInferenceVariable(this.s) && TypeHelper.isProperType(this.t)) {
         return Optional.of(new Instantiation((InferenceVariable)this.s, this.t));
      } else {
         return TypeHelper.isProperType(this.s) && TypeHelper.isInferenceVariable(this.t) ? Optional.of(new Instantiation((InferenceVariable)this.t, this.s)) : Optional.empty();
      }
   }

   public boolean isSatisfied(InferenceVariableSubstitution inferenceVariableSubstitution) {
      throw new UnsupportedOperationException();
   }
}
