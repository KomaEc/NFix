package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.resolution.MethodAmbiguityException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstructorResolutionLogic {
   private static List<ResolvedType> groupVariadicParamValues(List<ResolvedType> argumentsTypes, int startVariadic, ResolvedType variadicType) {
      List<ResolvedType> res = new ArrayList(argumentsTypes.subList(0, startVariadic));
      List<ResolvedType> variadicValues = argumentsTypes.subList(startVariadic, argumentsTypes.size());
      if (variadicValues.isEmpty()) {
         res.add(variadicType);
      } else {
         ResolvedType componentType = findCommonType(variadicValues);
         res.add(new ResolvedArrayType(componentType));
      }

      return res;
   }

   private static ResolvedType findCommonType(List<ResolvedType> variadicValues) {
      if (variadicValues.isEmpty()) {
         throw new IllegalArgumentException();
      } else {
         return (ResolvedType)variadicValues.get(0);
      }
   }

   public static boolean isApplicable(ResolvedConstructorDeclaration constructor, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      return isApplicable(constructor, argumentsTypes, typeSolver, false);
   }

   private static boolean isApplicable(ResolvedConstructorDeclaration constructor, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, boolean withWildcardTolerance) {
      if (constructor.hasVariadicParameter()) {
         int pos = constructor.getNumberOfParams() - 1;
         if (constructor.getNumberOfParams() == argumentsTypes.size()) {
            ResolvedType expectedType = constructor.getLastParam().getType();
            ResolvedType actualType = (ResolvedType)argumentsTypes.get(pos);
            if (!expectedType.isAssignableBy(actualType)) {
               ResolvedTypeParameterDeclaration tp;
               for(Iterator var7 = constructor.getTypeParameters().iterator(); var7.hasNext(); expectedType = MethodResolutionLogic.replaceTypeParam(expectedType, tp, typeSolver)) {
                  tp = (ResolvedTypeParameterDeclaration)var7.next();
               }

               if (!expectedType.isAssignableBy(actualType)) {
                  if (actualType.isArray() && expectedType.isAssignableBy(actualType.asArrayType().getComponentType())) {
                     argumentsTypes.set(pos, actualType.asArrayType().getComponentType());
                  } else {
                     argumentsTypes = groupVariadicParamValues(argumentsTypes, pos, constructor.getLastParam().getType());
                  }
               }
            }
         } else {
            if (pos > argumentsTypes.size()) {
               return false;
            }

            argumentsTypes = groupVariadicParamValues(argumentsTypes, pos, constructor.getLastParam().getType());
         }
      }

      if (constructor.getNumberOfParams() != argumentsTypes.size()) {
         return false;
      } else {
         Map<String, ResolvedType> matchedParameters = new HashMap();
         boolean needForWildCardTolerance = false;

         for(int i = 0; i < constructor.getNumberOfParams(); ++i) {
            ResolvedType expectedType = constructor.getParam(i).getType();
            ResolvedType actualType = (ResolvedType)argumentsTypes.get(i);
            if (expectedType.isTypeVariable() && !expectedType.isWildcard() && expectedType.asTypeParameter().declaredOnMethod()) {
               matchedParameters.put(expectedType.asTypeParameter().getName(), actualType);
            } else {
               boolean isAssignableWithoutSubstitution = expectedType.isAssignableBy(actualType) || constructor.getParam(i).isVariadic() && (new ResolvedArrayType(expectedType)).isAssignableBy(actualType);
               if (!isAssignableWithoutSubstitution && expectedType.isReferenceType() && actualType.isReferenceType()) {
                  isAssignableWithoutSubstitution = MethodResolutionLogic.isAssignableMatchTypeParameters((ResolvedReferenceType)expectedType.asReferenceType(), (ResolvedReferenceType)actualType.asReferenceType(), matchedParameters);
               }

               if (!isAssignableWithoutSubstitution) {
                  List<ResolvedTypeParameterDeclaration> typeParameters = constructor.getTypeParameters();
                  typeParameters.addAll(constructor.declaringType().getTypeParameters());

                  ResolvedTypeParameterDeclaration tp;
                  for(Iterator var11 = typeParameters.iterator(); var11.hasNext(); expectedType = MethodResolutionLogic.replaceTypeParam(expectedType, tp, typeSolver)) {
                     tp = (ResolvedTypeParameterDeclaration)var11.next();
                  }

                  if (!expectedType.isAssignableBy(actualType)) {
                     if (actualType.isWildcard() && withWildcardTolerance && !expectedType.isPrimitive()) {
                        needForWildCardTolerance = true;
                     } else if (!constructor.hasVariadicParameter() || i != constructor.getNumberOfParams() - 1 || !(new ResolvedArrayType(expectedType)).isAssignableBy(actualType)) {
                        return false;
                     }
                  }
               }
            }
         }

         return !withWildcardTolerance || needForWildCardTolerance;
      }
   }

   public static SymbolReference<ResolvedConstructorDeclaration> findMostApplicable(List<ResolvedConstructorDeclaration> constructors, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      SymbolReference<ResolvedConstructorDeclaration> res = findMostApplicable(constructors, argumentsTypes, typeSolver, false);
      return res.isSolved() ? res : findMostApplicable(constructors, argumentsTypes, typeSolver, true);
   }

   public static SymbolReference<ResolvedConstructorDeclaration> findMostApplicable(List<ResolvedConstructorDeclaration> constructors, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, boolean wildcardTolerance) {
      List<ResolvedConstructorDeclaration> applicableConstructors = (List)constructors.stream().filter((m) -> {
         return isApplicable(m, argumentsTypes, typeSolver, wildcardTolerance);
      }).collect(Collectors.toList());
      if (applicableConstructors.isEmpty()) {
         return SymbolReference.unsolved(ResolvedConstructorDeclaration.class);
      } else if (applicableConstructors.size() == 1) {
         return SymbolReference.solved((ResolvedDeclaration)applicableConstructors.get(0));
      } else {
         ResolvedConstructorDeclaration winningCandidate = (ResolvedConstructorDeclaration)applicableConstructors.get(0);
         ResolvedConstructorDeclaration other = null;
         boolean possibleAmbiguity = false;

         for(int i = 1; i < applicableConstructors.size(); ++i) {
            other = (ResolvedConstructorDeclaration)applicableConstructors.get(i);
            if (isMoreSpecific(winningCandidate, other, typeSolver)) {
               possibleAmbiguity = false;
            } else if (isMoreSpecific(other, winningCandidate, typeSolver)) {
               possibleAmbiguity = false;
               winningCandidate = other;
            } else if (winningCandidate.declaringType().getQualifiedName().equals(other.declaringType().getQualifiedName())) {
               possibleAmbiguity = true;
            }
         }

         if (possibleAmbiguity && !MethodResolutionLogic.isExactMatch(winningCandidate, argumentsTypes)) {
            if (!MethodResolutionLogic.isExactMatch(other, argumentsTypes)) {
               throw new MethodAmbiguityException("Ambiguous constructor call: cannot find a most applicable constructor: " + winningCandidate + ", " + other);
            }

            winningCandidate = other;
         }

         return SymbolReference.solved(winningCandidate);
      }
   }

   private static boolean isMoreSpecific(ResolvedConstructorDeclaration constructorA, ResolvedConstructorDeclaration constructorB, TypeSolver typeSolver) {
      boolean oneMoreSpecificFound = false;
      if (constructorA.getNumberOfParams() < constructorB.getNumberOfParams()) {
         return true;
      } else if (constructorA.getNumberOfParams() > constructorB.getNumberOfParams()) {
         return false;
      } else {
         for(int i = 0; i < constructorA.getNumberOfParams(); ++i) {
            ResolvedType tdA = constructorA.getParam(i).getType();
            ResolvedType tdB = constructorB.getParam(i).getType();
            if (tdB.isAssignableBy(tdA) && !tdA.isAssignableBy(tdB)) {
               oneMoreSpecificFound = true;
            }

            if (tdA.isAssignableBy(tdB) && !tdB.isAssignableBy(tdA)) {
               return false;
            }

            if (i == constructorA.getNumberOfParams() - 1 && tdA.arrayLevel() > tdB.arrayLevel()) {
               return true;
            }
         }

         return oneMoreSpecificFound;
      }
   }
}
