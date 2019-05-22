package com.github.javaparser.symbolsolver.resolution.typeinference.bounds;

import com.github.javaparser.symbolsolver.resolution.typeinference.Bound;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariable;
import com.github.javaparser.symbolsolver.resolution.typeinference.InferenceVariableSubstitution;
import java.util.Collections;
import java.util.Set;

public class FalseBound extends Bound {
   private static FalseBound INSTANCE = new FalseBound();

   private FalseBound() {
   }

   public static FalseBound getInstance() {
      return INSTANCE;
   }

   public String toString() {
      return "FalseBound{}";
   }

   public boolean isSatisfied(InferenceVariableSubstitution inferenceVariableSubstitution) {
      return false;
   }

   public Set<InferenceVariable> usedInferenceVariables() {
      return Collections.emptySet();
   }
}
