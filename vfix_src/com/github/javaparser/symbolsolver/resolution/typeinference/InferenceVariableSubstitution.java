package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.types.ResolvedType;
import java.util.LinkedList;
import java.util.List;

public class InferenceVariableSubstitution {
   private static final InferenceVariableSubstitution EMPTY = new InferenceVariableSubstitution();
   private List<InferenceVariable> inferenceVariables = new LinkedList();
   private List<ResolvedType> types = new LinkedList();

   public static InferenceVariableSubstitution empty() {
      return EMPTY;
   }

   private InferenceVariableSubstitution() {
   }

   public InferenceVariableSubstitution withPair(InferenceVariable inferenceVariable, ResolvedType type) {
      InferenceVariableSubstitution newInstance = new InferenceVariableSubstitution();
      newInstance.inferenceVariables.addAll(this.inferenceVariables);
      newInstance.types.addAll(this.types);
      newInstance.inferenceVariables.add(inferenceVariable);
      newInstance.types.add(type);
      return newInstance;
   }
}
