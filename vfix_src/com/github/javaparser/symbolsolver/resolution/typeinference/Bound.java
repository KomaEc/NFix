package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.FalseBound;
import java.util.Optional;
import java.util.Set;

public abstract class Bound {
   static Bound falseBound() {
      return FalseBound.getInstance();
   }

   public abstract boolean isSatisfied(InferenceVariableSubstitution var1);

   public Optional<Instantiation> isAnInstantiation() {
      return Optional.empty();
   }

   boolean isAnInstantiationFor(InferenceVariable v) {
      return this.isAnInstantiation().isPresent() && ((Instantiation)this.isAnInstantiation().get()).getInferenceVariable().equals(v);
   }

   public Optional<ProperUpperBound> isProperUpperBound() {
      return Optional.empty();
   }

   public Optional<ProperLowerBound> isProperLowerBound() {
      return Optional.empty();
   }

   Optional<ProperLowerBound> isProperLowerBoundFor(InferenceVariable inferenceVariable) {
      Optional<ProperLowerBound> partial = this.isProperLowerBound();
      return partial.isPresent() && ((ProperLowerBound)partial.get()).getInferenceVariable().equals(inferenceVariable) ? partial : Optional.empty();
   }

   Optional<ProperUpperBound> isProperUpperBoundFor(InferenceVariable inferenceVariable) {
      Optional<ProperUpperBound> partial = this.isProperUpperBound();
      return partial.isPresent() && ((ProperUpperBound)partial.get()).getInferenceVariable().equals(inferenceVariable) ? partial : Optional.empty();
   }

   public boolean isADependency() {
      return false;
   }

   boolean isThrowsBoundOn(InferenceVariable inferenceVariable) {
      return false;
   }

   public abstract Set<InferenceVariable> usedInferenceVariables();
}
