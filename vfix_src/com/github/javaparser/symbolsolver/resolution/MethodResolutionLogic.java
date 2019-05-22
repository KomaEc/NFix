package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.resolution.MethodAmbiguityException;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodLikeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnonymousClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistClassDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistEnumDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionEnumDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionInterfaceDeclaration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MethodResolutionLogic {
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

   public static boolean isApplicable(ResolvedMethodDeclaration method, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      return isApplicable(method, name, argumentsTypes, typeSolver, false);
   }

   private static boolean isApplicable(ResolvedMethodDeclaration method, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, boolean withWildcardTolerance) {
      if (!method.getName().equals(name)) {
         return false;
      } else {
         if (method.hasVariadicParameter()) {
            int pos = method.getNumberOfParams() - 1;
            if (method.getNumberOfParams() == argumentsTypes.size()) {
               ResolvedType expectedType = method.getLastParam().getType();
               ResolvedType actualType = (ResolvedType)argumentsTypes.get(pos);
               if (!expectedType.isAssignableBy(actualType)) {
                  ResolvedTypeParameterDeclaration tp;
                  for(Iterator var8 = method.getTypeParameters().iterator(); var8.hasNext(); expectedType = replaceTypeParam(expectedType, tp, typeSolver)) {
                     tp = (ResolvedTypeParameterDeclaration)var8.next();
                  }

                  if (!expectedType.isAssignableBy(actualType)) {
                     if (actualType.isArray() && expectedType.isAssignableBy(actualType.asArrayType().getComponentType())) {
                        argumentsTypes.set(pos, actualType.asArrayType().getComponentType());
                     } else {
                        argumentsTypes = groupVariadicParamValues(argumentsTypes, pos, method.getLastParam().getType());
                     }
                  }
               }
            } else {
               if (pos > argumentsTypes.size()) {
                  return false;
               }

               argumentsTypes = groupVariadicParamValues(argumentsTypes, pos, method.getLastParam().getType());
            }
         }

         if (method.getNumberOfParams() != argumentsTypes.size()) {
            return false;
         } else {
            Map<String, ResolvedType> matchedParameters = new HashMap();
            boolean needForWildCardTolerance = false;

            for(int i = 0; i < method.getNumberOfParams(); ++i) {
               ResolvedType expectedType = method.getParam(i).getType();
               ResolvedType actualType = (ResolvedType)argumentsTypes.get(i);
               if (expectedType.isTypeVariable() && !expectedType.isWildcard() && expectedType.asTypeParameter().declaredOnMethod()) {
                  matchedParameters.put(expectedType.asTypeParameter().getName(), actualType);
               } else {
                  boolean isAssignableWithoutSubstitution = expectedType.isAssignableBy(actualType) || method.getParam(i).isVariadic() && (new ResolvedArrayType(expectedType)).isAssignableBy(actualType);
                  if (!isAssignableWithoutSubstitution && expectedType.isReferenceType() && actualType.isReferenceType()) {
                     isAssignableWithoutSubstitution = isAssignableMatchTypeParameters((ResolvedReferenceType)expectedType.asReferenceType(), (ResolvedReferenceType)actualType.asReferenceType(), matchedParameters);
                  }

                  if (!isAssignableWithoutSubstitution) {
                     List<ResolvedTypeParameterDeclaration> typeParameters = method.getTypeParameters();
                     typeParameters.addAll(method.declaringType().getTypeParameters());

                     ResolvedTypeParameterDeclaration tp;
                     for(Iterator var12 = typeParameters.iterator(); var12.hasNext(); expectedType = replaceTypeParam(expectedType, tp, typeSolver)) {
                        tp = (ResolvedTypeParameterDeclaration)var12.next();
                     }

                     if (!expectedType.isAssignableBy(actualType)) {
                        if (actualType.isWildcard() && withWildcardTolerance && !expectedType.isPrimitive()) {
                           needForWildCardTolerance = true;
                        } else if (!method.hasVariadicParameter() || i != method.getNumberOfParams() - 1 || !(new ResolvedArrayType(expectedType)).isAssignableBy(actualType)) {
                           return false;
                        }
                     }
                  }
               }
            }

            return !withWildcardTolerance || needForWildCardTolerance;
         }
      }
   }

   public static boolean isAssignableMatchTypeParameters(ResolvedType expected, ResolvedType actual, Map<String, ResolvedType> matchedParameters) {
      if (expected.isReferenceType() && actual.isReferenceType()) {
         return isAssignableMatchTypeParameters(expected.asReferenceType(), actual.asReferenceType(), matchedParameters);
      } else if (expected.isTypeVariable()) {
         matchedParameters.put(expected.asTypeParameter().getName(), actual);
         return true;
      } else {
         throw new UnsupportedOperationException(expected.getClass().getCanonicalName() + " " + actual.getClass().getCanonicalName());
      }
   }

   public static boolean isAssignableMatchTypeParameters(ResolvedReferenceType expected, ResolvedReferenceType actual, Map<String, ResolvedType> matchedParameters) {
      if (actual.getQualifiedName().equals(expected.getQualifiedName())) {
         return isAssignableMatchTypeParametersMatchingQName(expected, actual, matchedParameters);
      } else {
         List<ResolvedReferenceType> ancestors = actual.getAllAncestors();
         Iterator var4 = ancestors.iterator();

         ResolvedReferenceType ancestor;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            ancestor = (ResolvedReferenceType)var4.next();
         } while(!isAssignableMatchTypeParametersMatchingQName(expected, ancestor, matchedParameters));

         return true;
      }
   }

   private static boolean isAssignableMatchTypeParametersMatchingQName(ResolvedReferenceType expected, ResolvedReferenceType actual, Map<String, ResolvedType> matchedParameters) {
      if (!expected.getQualifiedName().equals(actual.getQualifiedName())) {
         return false;
      } else if (expected.typeParametersValues().size() != actual.typeParametersValues().size()) {
         throw new UnsupportedOperationException();
      } else {
         for(int i = 0; i < expected.typeParametersValues().size(); ++i) {
            ResolvedType expectedParam = (ResolvedType)expected.typeParametersValues().get(i);
            ResolvedType actualParam = (ResolvedType)actual.typeParametersValues().get(i);
            if (expectedParam.isReferenceType() && actualParam.isReferenceType()) {
               ResolvedReferenceType r1 = expectedParam.asReferenceType();
               ResolvedReferenceType r2 = actualParam.asReferenceType();
               return isAssignableMatchTypeParametersMatchingQName(r1, r2, matchedParameters);
            }

            if (expectedParam.isTypeVariable()) {
               String expectedParamName = expectedParam.asTypeParameter().getName();
               if (!actualParam.isTypeVariable() || !actualParam.asTypeParameter().getName().equals(expectedParamName)) {
                  return matchTypeVariable(expectedParam.asTypeVariable(), actualParam, matchedParameters);
               }
            } else {
               if (!expectedParam.isReferenceType()) {
                  if (expectedParam.isWildcard()) {
                     if (expectedParam.asWildcard().isExtends()) {
                        return isAssignableMatchTypeParameters((ResolvedType)expectedParam.asWildcard().getBoundedType(), (ResolvedType)actual, matchedParameters);
                     }

                     return true;
                  }

                  throw new UnsupportedOperationException(expectedParam.describe());
               }

               if (actualParam.isTypeVariable()) {
                  return matchTypeVariable(actualParam.asTypeVariable(), expectedParam, matchedParameters);
               }

               if (!expectedParam.equals(actualParam)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private static boolean matchTypeVariable(ResolvedTypeVariable typeVariable, ResolvedType type, Map<String, ResolvedType> matchedParameters) {
      String typeParameterName = typeVariable.asTypeParameter().getName();
      if (matchedParameters.containsKey(typeParameterName)) {
         ResolvedType matchedParameter = (ResolvedType)matchedParameters.get(typeParameterName);
         if (matchedParameter.isAssignableBy(type)) {
            return true;
         } else if (type.isAssignableBy(matchedParameter)) {
            matchedParameters.put(typeParameterName, type);
            return true;
         } else {
            return false;
         }
      } else {
         matchedParameters.put(typeParameterName, type);
         return true;
      }
   }

   public static ResolvedType replaceTypeParam(ResolvedType type, ResolvedTypeParameterDeclaration tp, TypeSolver typeSolver) {
      if (!type.isTypeVariable() && !type.isWildcard()) {
         if (type.isPrimitive()) {
            return type;
         } else if (type.isArray()) {
            return new ResolvedArrayType(replaceTypeParam(type.asArrayType().getComponentType(), tp, typeSolver));
         } else if (type.isReferenceType()) {
            ResolvedReferenceType result = type.asReferenceType();
            result = result.transformTypeParameters((typeParam) -> {
               return replaceTypeParam(typeParam, tp, typeSolver);
            }).asReferenceType();
            return result;
         } else {
            throw new UnsupportedOperationException("Replacing " + type + ", param " + tp + " with " + type.getClass().getCanonicalName());
         }
      } else if (type.describe().equals(tp.getName())) {
         List<ResolvedTypeParameterDeclaration.Bound> bounds = tp.getBounds();
         if (bounds.size() > 1) {
            throw new UnsupportedOperationException();
         } else {
            return (ResolvedType)(bounds.size() == 1 ? ((ResolvedTypeParameterDeclaration.Bound)bounds.get(0)).getType() : new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver));
         }
      } else {
         return type;
      }
   }

   public static boolean isApplicable(MethodUsage method, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      if (!method.getName().equals(name)) {
         return false;
      } else if (method.getNoParams() != argumentsTypes.size()) {
         return false;
      } else {
         for(int i = 0; i < method.getNoParams(); ++i) {
            ResolvedType expectedType = method.getParamType(i);
            ResolvedType expectedTypeWithoutSubstitutions = expectedType;
            ResolvedType expectedTypeWithInference = method.getParamType(i);
            ResolvedType actualType = (ResolvedType)argumentsTypes.get(i);
            List<ResolvedTypeParameterDeclaration> typeParameters = method.getDeclaration().getTypeParameters();
            typeParameters.addAll(method.declaringType().getTypeParameters());
            if (expectedType.describe().equals(actualType.describe())) {
               return true;
            }

            Map<ResolvedTypeParameterDeclaration, ResolvedType> derivedValues = new HashMap();

            for(int j = 0; j < method.getParamTypes().size(); ++j) {
               ResolvedParameterDeclaration parameter = method.getDeclaration().getParam(i);
               ResolvedType parameterType = parameter.getType();
               if (parameter.isVariadic()) {
                  parameterType = parameterType.asArrayType().getComponentType();
               }

               inferTypes((ResolvedType)argumentsTypes.get(j), parameterType, derivedValues);
            }

            Iterator var15;
            Entry entry;
            ResolvedTypeParameterDeclaration tp;
            for(var15 = derivedValues.entrySet().iterator(); var15.hasNext(); expectedTypeWithInference = expectedTypeWithInference.replaceTypeVariables(tp, (ResolvedType)entry.getValue())) {
               entry = (Entry)var15.next();
               tp = (ResolvedTypeParameterDeclaration)entry.getKey();
            }

            var15 = typeParameters.iterator();

            while(var15.hasNext()) {
               ResolvedTypeParameterDeclaration tp = (ResolvedTypeParameterDeclaration)var15.next();
               if (tp.getBounds().isEmpty()) {
                  expectedType = expectedType.replaceTypeVariables(tp, ResolvedWildcard.extendsBound(new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver)));
               } else {
                  if (tp.getBounds().size() != 1) {
                     throw new UnsupportedOperationException();
                  }

                  ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)tp.getBounds().get(0);
                  if (bound.isExtends()) {
                     expectedType = expectedType.replaceTypeVariables(tp, ResolvedWildcard.extendsBound(bound.getType()));
                  } else {
                     expectedType = expectedType.replaceTypeVariables(tp, ResolvedWildcard.superBound(bound.getType()));
                  }
               }
            }

            ResolvedType expectedType2 = expectedType;
            Iterator var19 = typeParameters.iterator();

            while(var19.hasNext()) {
               tp = (ResolvedTypeParameterDeclaration)var19.next();
               if (tp.getBounds().isEmpty()) {
                  expectedType2 = expectedType2.replaceTypeVariables(tp, new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver));
               } else {
                  if (tp.getBounds().size() != 1) {
                     throw new UnsupportedOperationException();
                  }

                  ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)tp.getBounds().get(0);
                  if (bound.isExtends()) {
                     expectedType2 = expectedType2.replaceTypeVariables(tp, bound.getType());
                  } else {
                     expectedType2 = expectedType2.replaceTypeVariables(tp, new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver));
                  }
               }
            }

            if (!expectedType.isAssignableBy(actualType) && !expectedType2.isAssignableBy(actualType) && !expectedTypeWithInference.isAssignableBy(actualType) && !expectedTypeWithoutSubstitutions.isAssignableBy(actualType)) {
               return false;
            }
         }

         return true;
      }
   }

   private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
      Set<Object> seen = ConcurrentHashMap.newKeySet();
      return (t) -> {
         return seen.add(keyExtractor.apply(t));
      };
   }

   public static SymbolReference<ResolvedMethodDeclaration> findMostApplicable(List<ResolvedMethodDeclaration> methods, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      SymbolReference<ResolvedMethodDeclaration> res = findMostApplicable(methods, name, argumentsTypes, typeSolver, false);
      return res.isSolved() ? res : findMostApplicable(methods, name, argumentsTypes, typeSolver, true);
   }

   public static SymbolReference<ResolvedMethodDeclaration> findMostApplicable(List<ResolvedMethodDeclaration> methods, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, boolean wildcardTolerance) {
      List<ResolvedMethodDeclaration> methodsWithMatchingName = (List)methods.stream().filter((m) -> {
         return m.getName().equals(name);
      }).collect(Collectors.toList());
      List<ResolvedMethodDeclaration> applicableMethods = (List)methodsWithMatchingName.stream().filter(distinctByKey(ResolvedMethodLikeDeclaration::getQualifiedSignature)).filter((m) -> {
         return isApplicable(m, name, argumentsTypes, typeSolver, wildcardTolerance);
      }).collect(Collectors.toList());
      if (applicableMethods.isEmpty()) {
         return SymbolReference.unsolved(ResolvedMethodDeclaration.class);
      } else {
         if (applicableMethods.size() > 1) {
            List<Integer> nullParamIndexes = new ArrayList();

            for(int i = 0; i < argumentsTypes.size(); ++i) {
               if (((ResolvedType)argumentsTypes.get(i)).isNull()) {
                  nullParamIndexes.add(i);
               }
            }

            if (!nullParamIndexes.isEmpty()) {
               Set<ResolvedMethodDeclaration> removeCandidates = new HashSet();
               Iterator var9 = nullParamIndexes.iterator();

               while(var9.hasNext()) {
                  Integer nullParamIndex = (Integer)var9.next();
                  Iterator var11 = applicableMethods.iterator();

                  while(var11.hasNext()) {
                     ResolvedMethodDeclaration methDecl = (ResolvedMethodDeclaration)var11.next();
                     if (methDecl.getParam(nullParamIndex).getType().isArray()) {
                        removeCandidates.add(methDecl);
                     }
                  }
               }

               if (!removeCandidates.isEmpty() && removeCandidates.size() < applicableMethods.size()) {
                  applicableMethods.removeAll(removeCandidates);
               }
            }
         }

         if (applicableMethods.size() == 1) {
            return SymbolReference.solved((ResolvedDeclaration)applicableMethods.get(0));
         } else {
            ResolvedMethodDeclaration winningCandidate = (ResolvedMethodDeclaration)applicableMethods.get(0);
            ResolvedMethodDeclaration other = null;
            boolean possibleAmbiguity = false;

            for(int i = 1; i < applicableMethods.size(); ++i) {
               other = (ResolvedMethodDeclaration)applicableMethods.get(i);
               if (isMoreSpecific(winningCandidate, other, argumentsTypes)) {
                  possibleAmbiguity = false;
               } else if (isMoreSpecific(other, winningCandidate, argumentsTypes)) {
                  possibleAmbiguity = false;
                  winningCandidate = other;
               } else if (winningCandidate.declaringType().getQualifiedName().equals(other.declaringType().getQualifiedName())) {
                  possibleAmbiguity = true;
               }
            }

            if (possibleAmbiguity && !isExactMatch(winningCandidate, argumentsTypes)) {
               if (!isExactMatch(other, argumentsTypes)) {
                  throw new MethodAmbiguityException("Ambiguous method call: cannot find a most applicable method: " + winningCandidate + ", " + other);
               }

               winningCandidate = other;
            }

            return SymbolReference.solved(winningCandidate);
         }
      }
   }

   protected static boolean isExactMatch(ResolvedMethodLikeDeclaration method, List<ResolvedType> argumentsTypes) {
      for(int i = 0; i < method.getNumberOfParams(); ++i) {
         if (!method.getParam(i).getType().equals(argumentsTypes.get(i))) {
            return false;
         }
      }

      return true;
   }

   private static boolean isMoreSpecific(ResolvedMethodDeclaration methodA, ResolvedMethodDeclaration methodB, List<ResolvedType> argumentTypes) {
      boolean oneMoreSpecificFound = false;
      if (methodA.getNumberOfParams() < methodB.getNumberOfParams()) {
         return true;
      } else if (methodA.getNumberOfParams() > methodB.getNumberOfParams()) {
         return false;
      } else {
         int lastIndex;
         for(lastIndex = 0; lastIndex < methodA.getNumberOfParams(); ++lastIndex) {
            ResolvedType tdA = methodA.getParam(lastIndex).getType();
            ResolvedType tdB = methodB.getParam(lastIndex).getType();
            if (tdB.isAssignableBy(tdA) && !tdA.isAssignableBy(tdB)) {
               oneMoreSpecificFound = true;
            }

            if (tdA.isAssignableBy(tdB) && !tdB.isAssignableBy(tdA)) {
               return false;
            }
         }

         if (!oneMoreSpecificFound) {
            lastIndex = argumentTypes.size() - 1;
            if (methodA.hasVariadicParameter() && !methodB.hasVariadicParameter()) {
               if (((ResolvedType)argumentTypes.get(lastIndex)).isArray()) {
                  return true;
               }

               if (!((ResolvedType)argumentTypes.get(lastIndex)).isArray()) {
                  return false;
               }
            }

            if (!methodA.hasVariadicParameter() && methodB.hasVariadicParameter()) {
               if (((ResolvedType)argumentTypes.get(lastIndex)).isArray()) {
                  return false;
               }

               if (!((ResolvedType)argumentTypes.get(lastIndex)).isArray()) {
                  return true;
               }
            }
         }

         return oneMoreSpecificFound;
      }
   }

   private static boolean isMoreSpecific(MethodUsage methodA, MethodUsage methodB) {
      boolean oneMoreSpecificFound = false;

      for(int i = 0; i < methodA.getNoParams(); ++i) {
         ResolvedType tdA = methodA.getParamType(i);
         ResolvedType tdB = methodB.getParamType(i);
         boolean aIsAssignableByB = tdA.isAssignableBy(tdB);
         boolean bIsAssignableByA = tdB.isAssignableBy(tdA);
         if (bIsAssignableByA && !aIsAssignableByB) {
            oneMoreSpecificFound = true;
         }

         if (aIsAssignableByB && !bIsAssignableByA) {
            return false;
         }
      }

      return oneMoreSpecificFound;
   }

   public static Optional<MethodUsage> findMostApplicableUsage(List<MethodUsage> methods, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      List<MethodUsage> applicableMethods = (List)methods.stream().filter((m) -> {
         return isApplicable(m, name, argumentsTypes, typeSolver);
      }).collect(Collectors.toList());
      if (applicableMethods.isEmpty()) {
         return Optional.empty();
      } else if (applicableMethods.size() == 1) {
         return Optional.of(applicableMethods.get(0));
      } else {
         MethodUsage winningCandidate = (MethodUsage)applicableMethods.get(0);

         for(int i = 1; i < applicableMethods.size(); ++i) {
            MethodUsage other = (MethodUsage)applicableMethods.get(i);
            if (!isMoreSpecific(winningCandidate, other)) {
               if (isMoreSpecific(other, winningCandidate)) {
                  winningCandidate = other;
               } else if (winningCandidate.declaringType().getQualifiedName().equals(other.declaringType().getQualifiedName()) && !areOverride(winningCandidate, other)) {
                  throw new MethodAmbiguityException("Ambiguous method call: cannot find a most applicable method: " + winningCandidate + ", " + other + ". First declared in " + winningCandidate.declaringType().getQualifiedName());
               }
            }
         }

         return Optional.of(winningCandidate);
      }
   }

   private static boolean areOverride(MethodUsage winningCandidate, MethodUsage other) {
      if (!winningCandidate.getName().equals(other.getName())) {
         return false;
      } else if (winningCandidate.getNoParams() != other.getNoParams()) {
         return false;
      } else {
         for(int i = 0; i < winningCandidate.getNoParams(); ++i) {
            if (!((ResolvedType)winningCandidate.getParamTypes().get(i)).equals(other.getParamTypes().get(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static SymbolReference<ResolvedMethodDeclaration> solveMethodInType(ResolvedTypeDeclaration typeDeclaration, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      return solveMethodInType(typeDeclaration, name, argumentsTypes, false, typeSolver);
   }

   public static SymbolReference<ResolvedMethodDeclaration> solveMethodInType(ResolvedTypeDeclaration typeDeclaration, String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      Context ctx;
      if (typeDeclaration instanceof JavaParserClassDeclaration) {
         ctx = ((JavaParserClassDeclaration)typeDeclaration).getContext();
         return ctx.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
      } else if (typeDeclaration instanceof JavaParserInterfaceDeclaration) {
         ctx = ((JavaParserInterfaceDeclaration)typeDeclaration).getContext();
         return ctx.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
      } else if (typeDeclaration instanceof JavaParserEnumDeclaration) {
         if (name.equals("values") && argumentsTypes.isEmpty()) {
            return SymbolReference.solved(new JavaParserEnumDeclaration.ValuesMethod((JavaParserEnumDeclaration)typeDeclaration, typeSolver));
         } else {
            ctx = ((JavaParserEnumDeclaration)typeDeclaration).getContext();
            return ctx.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
         }
      } else if (typeDeclaration instanceof JavaParserAnonymousClassDeclaration) {
         ctx = ((JavaParserAnonymousClassDeclaration)typeDeclaration).getContext();
         return ctx.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
      } else if (typeDeclaration instanceof ReflectionClassDeclaration) {
         return ((ReflectionClassDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else if (typeDeclaration instanceof ReflectionInterfaceDeclaration) {
         return ((ReflectionInterfaceDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else if (typeDeclaration instanceof ReflectionEnumDeclaration) {
         return ((ReflectionEnumDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else if (typeDeclaration instanceof JavassistInterfaceDeclaration) {
         return ((JavassistInterfaceDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else if (typeDeclaration instanceof JavassistClassDeclaration) {
         return ((JavassistClassDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else if (typeDeclaration instanceof JavassistEnumDeclaration) {
         return ((JavassistEnumDeclaration)typeDeclaration).solveMethod(name, argumentsTypes, staticOnly);
      } else {
         throw new UnsupportedOperationException(typeDeclaration.getClass().getCanonicalName());
      }
   }

   private static void inferTypes(ResolvedType source, ResolvedType target, Map<ResolvedTypeParameterDeclaration, ResolvedType> mappings) {
      if (!source.equals(target)) {
         if (source.isReferenceType() && target.isReferenceType()) {
            ResolvedReferenceType sourceRefType = source.asReferenceType();
            ResolvedReferenceType targetRefType = target.asReferenceType();
            if (sourceRefType.getQualifiedName().equals(targetRefType.getQualifiedName()) && !sourceRefType.isRawType() && !targetRefType.isRawType()) {
               for(int i = 0; i < sourceRefType.typeParametersValues().size(); ++i) {
                  inferTypes((ResolvedType)sourceRefType.typeParametersValues().get(i), (ResolvedType)targetRefType.typeParametersValues().get(i), mappings);
               }
            }

         } else if (source.isReferenceType() && target.isWildcard()) {
            if (target.asWildcard().isBounded()) {
               inferTypes(source, target.asWildcard().getBoundedType(), mappings);
            }
         } else if (!source.isWildcard() || !target.isWildcard()) {
            if (source.isReferenceType() && target.isTypeVariable()) {
               mappings.put(target.asTypeParameter(), source);
            } else if (source.isWildcard() && target.isReferenceType()) {
               if (source.asWildcard().isBounded()) {
                  inferTypes(source.asWildcard().getBoundedType(), target, mappings);
               }

            } else if (source.isWildcard() && target.isTypeVariable()) {
               mappings.put(target.asTypeParameter(), source);
            } else if (source.isTypeVariable() && target.isTypeVariable()) {
               mappings.put(target.asTypeParameter(), source);
            } else if (!source.isPrimitive() && !target.isPrimitive()) {
               if (!source.isNull()) {
                  ;
               }
            }
         }
      }
   }
}
