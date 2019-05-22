package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TypeInferenceCache {
   private static Map<TypeSolver, IdentityHashMap<LambdaExpr, Map<String, ResolvedType>>> typeForLambdaParameters = new HashMap();
   private static Map<TypeSolver, IdentityHashMap<LambdaExpr, List<InferenceVariable>>> inferenceVariables = new HashMap();

   public static void record(TypeSolver typeSolver, LambdaExpr lambdaExpr, String paramName, ResolvedType type) {
      if (!typeForLambdaParameters.containsKey(typeSolver)) {
         typeForLambdaParameters.put(typeSolver, new IdentityHashMap());
      }

      if (!((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).containsKey(lambdaExpr)) {
         ((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).put(lambdaExpr, new HashMap());
      }

      ((Map)((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).get(lambdaExpr)).put(paramName, type);
   }

   public static Optional<ResolvedType> retrieve(TypeSolver typeSolver, LambdaExpr lambdaExpr, String paramName) {
      if (!typeForLambdaParameters.containsKey(typeSolver)) {
         return Optional.empty();
      } else if (!((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).containsKey(lambdaExpr)) {
         return Optional.empty();
      } else {
         return !((Map)((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).get(lambdaExpr)).containsKey(paramName) ? Optional.empty() : Optional.of(((Map)((IdentityHashMap)typeForLambdaParameters.get(typeSolver)).get(lambdaExpr)).get(paramName));
      }
   }

   public static void recordInferenceVariables(TypeSolver typeSolver, LambdaExpr lambdaExpr, List<InferenceVariable> _inferenceVariables) {
      if (!inferenceVariables.containsKey(typeSolver)) {
         inferenceVariables.put(typeSolver, new IdentityHashMap());
      }

      ((IdentityHashMap)inferenceVariables.get(typeSolver)).put(lambdaExpr, _inferenceVariables);
   }

   public static Optional<List<InferenceVariable>> retrieveInferenceVariables(TypeSolver typeSolver, LambdaExpr lambdaExpr) {
      if (!inferenceVariables.containsKey(typeSolver)) {
         return Optional.empty();
      } else {
         return !((IdentityHashMap)inferenceVariables.get(typeSolver)).containsKey(lambdaExpr) ? Optional.empty() : Optional.of(((IdentityHashMap)inferenceVariables.get(typeSolver)).get(lambdaExpr));
      }
   }
}
