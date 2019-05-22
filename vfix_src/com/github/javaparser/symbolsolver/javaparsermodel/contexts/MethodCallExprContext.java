package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedLambdaConstraintType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedUnionType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import com.github.javaparser.utils.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public class MethodCallExprContext extends AbstractJavaParserContext<MethodCallExpr> {
   public MethodCallExprContext(MethodCallExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public Optional<ResolvedType> solveGenericType(String name, TypeSolver typeSolver) {
      if (((MethodCallExpr)this.wrappedNode).getScope().isPresent()) {
         ResolvedType typeOfScope = JavaParserFacade.get(typeSolver).getType((Node)((MethodCallExpr)this.wrappedNode).getScope().get());
         Optional<ResolvedType> res = typeOfScope.asReferenceType().getGenericParameterByName(name);
         return res;
      } else {
         return Optional.empty();
      }
   }

   public String toString() {
      return "MethodCallExprContext{wrapped=" + this.wrappedNode + "}";
   }

   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      if (((MethodCallExpr)this.wrappedNode).getScope().isPresent()) {
         Expression scope = (Expression)((MethodCallExpr)this.wrappedNode).getScope().get();
         if (scope instanceof NameExpr) {
            String className = ((NameExpr)scope).getName().getId();
            SymbolReference<ResolvedTypeDeclaration> ref = this.solveType(className, typeSolver);
            if (ref.isSolved()) {
               SymbolReference<ResolvedMethodDeclaration> m = MethodResolutionLogic.solveMethodInType((ResolvedTypeDeclaration)ref.getCorrespondingDeclaration(), name, argumentsTypes, typeSolver);
               if (m.isSolved()) {
                  MethodUsage methodUsage = new MethodUsage((ResolvedMethodDeclaration)m.getCorrespondingDeclaration());
                  methodUsage = this.resolveMethodTypeParametersFromExplicitList(typeSolver, methodUsage);
                  methodUsage = this.resolveMethodTypeParameters(methodUsage, argumentsTypes);
                  return Optional.of(methodUsage);
               }

               throw new UnsolvedSymbolException(((ResolvedTypeDeclaration)ref.getCorrespondingDeclaration()).toString(), "Method '" + name + "' with parameterTypes " + argumentsTypes);
            }
         }

         ResolvedType typeOfScope = JavaParserFacade.get(typeSolver).getType(scope);
         Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes = new HashMap();

         int i;
         ResolvedType updatedArgumentType;
         for(i = 0; i < argumentsTypes.size(); ++i) {
            updatedArgumentType = (ResolvedType)argumentsTypes.get(i);
            ResolvedType updatedArgumentType = this.usingParameterTypesFromScope(typeOfScope, updatedArgumentType, inferredTypes);
            argumentsTypes.set(i, updatedArgumentType);
         }

         for(i = 0; i < argumentsTypes.size(); ++i) {
            updatedArgumentType = this.applyInferredTypes((ResolvedType)argumentsTypes.get(i), inferredTypes);
            argumentsTypes.set(i, updatedArgumentType);
         }

         return this.solveMethodAsUsage((ResolvedType)typeOfScope, name, argumentsTypes, typeSolver, this);
      } else {
         Context parentContext;
         for(parentContext = this.getParent(); parentContext instanceof MethodCallExprContext || parentContext instanceof ObjectCreationContext; parentContext = parentContext.getParent()) {
         }

         return parentContext.solveMethodAsUsage(name, argumentsTypes, typeSolver);
      }
   }

   private MethodUsage resolveMethodTypeParametersFromExplicitList(TypeSolver typeSolver, MethodUsage methodUsage) {
      if (((MethodCallExpr)this.wrappedNode).getTypeArguments().isPresent()) {
         List<ResolvedType> typeArguments = new ArrayList();
         Iterator var4 = ((NodeList)((MethodCallExpr)this.wrappedNode).getTypeArguments().get()).iterator();

         while(var4.hasNext()) {
            Type ty = (Type)var4.next();
            typeArguments.add(JavaParserFacade.get(typeSolver).convertToUsage(ty));
         }

         List<ResolvedTypeParameterDeclaration> tyParamDecls = methodUsage.getDeclaration().getTypeParameters();
         if (tyParamDecls.size() == typeArguments.size()) {
            for(int i = 0; i < tyParamDecls.size(); ++i) {
               methodUsage = methodUsage.replaceTypeParameter((ResolvedTypeParameterDeclaration)tyParamDecls.get(i), (ResolvedType)typeArguments.get(i));
            }
         }
      }

      return methodUsage;
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      return this.getParent().solveSymbol(name, typeSolver);
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      Context parentContext = this.getParent();
      return parentContext.solveSymbolAsValue(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      Collection<ResolvedReferenceTypeDeclaration> rrtds = this.findTypeDeclarations(((MethodCallExpr)this.wrappedNode).getScope(), typeSolver);
      Iterator var6 = rrtds.iterator();

      SymbolReference res;
      do {
         if (!var6.hasNext()) {
            return SymbolReference.unsolved(ResolvedMethodDeclaration.class);
         }

         ResolvedReferenceTypeDeclaration rrtd = (ResolvedReferenceTypeDeclaration)var6.next();
         res = MethodResolutionLogic.solveMethodInType(rrtd, name, argumentsTypes, false, typeSolver);
      } while(!res.isSolved());

      return res;
   }

   private Optional<MethodUsage> solveMethodAsUsage(ResolvedReferenceType refType, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext) {
      Optional<MethodUsage> ref = ContextHelper.solveMethodAsUsage(refType.getTypeDeclaration(), name, argumentsTypes, typeSolver, invokationContext, refType.typeParametersValues());
      if (!ref.isPresent()) {
         return ref;
      } else {
         MethodUsage methodUsage = (MethodUsage)ref.get();
         methodUsage = this.resolveMethodTypeParametersFromExplicitList(typeSolver, methodUsage);
         Map<ResolvedTypeParameterDeclaration, ResolvedType> derivedValues = new HashMap();

         ResolvedType replaced;
         for(int i = 0; i < methodUsage.getParamTypes().size(); ++i) {
            ResolvedParameterDeclaration parameter = methodUsage.getDeclaration().getParam(i);
            replaced = parameter.getType();
            if (parameter.isVariadic()) {
               replaced = replaced.asArrayType().getComponentType();
            }

            this.inferTypes((ResolvedType)argumentsTypes.get(i), replaced, derivedValues);
         }

         Entry entry;
         for(Iterator var12 = derivedValues.entrySet().iterator(); var12.hasNext(); methodUsage = methodUsage.replaceTypeParameter((ResolvedTypeParameterDeclaration)entry.getKey(), (ResolvedType)entry.getValue())) {
            entry = (Entry)var12.next();
         }

         ResolvedType returnType = refType.useThisTypeParametersOnTheGivenType(methodUsage.returnType());
         if (returnType != methodUsage.returnType()) {
            methodUsage = methodUsage.replaceReturnType(returnType);
         }

         for(int i = 0; i < methodUsage.getParamTypes().size(); ++i) {
            replaced = refType.useThisTypeParametersOnTheGivenType((ResolvedType)methodUsage.getParamTypes().get(i));
            methodUsage = methodUsage.replaceParamType(i, replaced);
         }

         return Optional.of(methodUsage);
      }
   }

   private void inferTypes(ResolvedType source, ResolvedType target, Map<ResolvedTypeParameterDeclaration, ResolvedType> mappings) {
      if (!source.equals(target)) {
         if (source.isReferenceType() && target.isReferenceType()) {
            ResolvedReferenceType sourceRefType = source.asReferenceType();
            ResolvedReferenceType targetRefType = target.asReferenceType();
            if (sourceRefType.getQualifiedName().equals(targetRefType.getQualifiedName()) && !sourceRefType.isRawType() && !targetRefType.isRawType()) {
               for(int i = 0; i < sourceRefType.typeParametersValues().size(); ++i) {
                  this.inferTypes((ResolvedType)sourceRefType.typeParametersValues().get(i), (ResolvedType)targetRefType.typeParametersValues().get(i), mappings);
               }
            }

         } else if (source.isReferenceType() && target.isWildcard()) {
            if (target.asWildcard().isBounded()) {
               this.inferTypes(source, target.asWildcard().getBoundedType(), mappings);
            }
         } else if (source.isWildcard() && target.isWildcard()) {
            if (source.asWildcard().isBounded() && target.asWildcard().isBounded()) {
               this.inferTypes(source.asWildcard().getBoundedType(), target.asWildcard().getBoundedType(), mappings);
            }

         } else if (source.isReferenceType() && target.isTypeVariable()) {
            mappings.put(target.asTypeParameter(), source);
         } else if (source.isWildcard() && target.isTypeVariable()) {
            mappings.put(target.asTypeParameter(), source);
         } else if (source.isArray() && target.isWildcard()) {
            if (target.asWildcard().isBounded()) {
               this.inferTypes(source, target.asWildcard().getBoundedType(), mappings);
            }
         } else if (source.isArray() && target.isTypeVariable()) {
            mappings.put(target.asTypeParameter(), source);
         } else if (source.isWildcard() && target.isReferenceType()) {
            if (source.asWildcard().isBounded()) {
               this.inferTypes(source.asWildcard().getBoundedType(), target, mappings);
            }

         } else if (source.isConstraint() && target.isReferenceType()) {
            this.inferTypes(source.asConstraintType().getBound(), target, mappings);
         } else if (source.isConstraint() && target.isTypeVariable()) {
            this.inferTypes(source.asConstraintType().getBound(), target, mappings);
         } else if (source.isTypeVariable() && target.isTypeVariable()) {
            mappings.put(target.asTypeParameter(), source);
         } else if (!source.isPrimitive() && !target.isPrimitive()) {
            if (!source.isNull()) {
               throw new RuntimeException(source.describe() + " " + target.describe());
            }
         }
      }
   }

   private MethodUsage resolveMethodTypeParameters(MethodUsage methodUsage, List<ResolvedType> actualParamTypes) {
      Map<ResolvedTypeParameterDeclaration, ResolvedType> matchedTypeParameters = new HashMap();
      if (methodUsage.getDeclaration().hasVariadicParameter()) {
         if (actualParamTypes.size() != methodUsage.getDeclaration().getNumberOfParams()) {
            return methodUsage;
         }

         ResolvedType expectedType = methodUsage.getDeclaration().getLastParam().getType().asArrayType().getComponentType();
         ResolvedType actualType = ((ResolvedType)actualParamTypes.get(actualParamTypes.size() - 1)).isArray() ? ((ResolvedType)actualParamTypes.get(actualParamTypes.size() - 1)).asArrayType().getComponentType() : (ResolvedType)actualParamTypes.get(actualParamTypes.size() - 1);
         ResolvedTypeParameterDeclaration tp;
         if (!expectedType.isAssignableBy(actualType)) {
            for(Iterator var6 = methodUsage.getDeclaration().getTypeParameters().iterator(); var6.hasNext(); expectedType = MethodResolutionLogic.replaceTypeParam(expectedType, tp, this.typeSolver)) {
               tp = (ResolvedTypeParameterDeclaration)var6.next();
            }
         }

         if (!expectedType.isAssignableBy(actualType)) {
            throw new UnsupportedOperationException(String.format("Unable to resolve the type typeParametersValues in a MethodUsage. Expected type: %s, Actual type: %s. Method Declaration: %s. MethodUsage: %s", expectedType, actualType, methodUsage.getDeclaration(), methodUsage));
         }

         this.matchTypeParameters(expectedType, actualType, matchedTypeParameters);
      }

      int until = methodUsage.getDeclaration().hasVariadicParameter() ? actualParamTypes.size() - 1 : actualParamTypes.size();

      for(int i = 0; i < until; ++i) {
         ResolvedType expectedType = methodUsage.getParamType(i);
         ResolvedType actualType = (ResolvedType)actualParamTypes.get(i);
         this.matchTypeParameters(expectedType, actualType, matchedTypeParameters);
      }

      ResolvedTypeParameterDeclaration tp;
      for(Iterator var10 = matchedTypeParameters.keySet().iterator(); var10.hasNext(); methodUsage = methodUsage.replaceTypeParameter(tp, (ResolvedType)matchedTypeParameters.get(tp))) {
         tp = (ResolvedTypeParameterDeclaration)var10.next();
      }

      return methodUsage;
   }

   private void matchTypeParameters(ResolvedType expectedType, ResolvedType actualType, Map<ResolvedTypeParameterDeclaration, ResolvedType> matchedTypeParameters) {
      if (expectedType.isTypeVariable()) {
         if (!actualType.isTypeVariable() && !actualType.isReferenceType()) {
            throw new UnsupportedOperationException(actualType.getClass().getCanonicalName());
         }

         matchedTypeParameters.put(expectedType.asTypeParameter(), actualType);
      } else if (expectedType.isArray()) {
         if (!actualType.isArray()) {
            throw new UnsupportedOperationException(actualType.getClass().getCanonicalName());
         }

         this.matchTypeParameters(expectedType.asArrayType().getComponentType(), actualType.asArrayType().getComponentType(), matchedTypeParameters);
      } else if (expectedType.isReferenceType()) {
         if (actualType.isReferenceType() && actualType.asReferenceType().typeParametersValues().size() > 0) {
            int i = 0;

            for(Iterator var5 = expectedType.asReferenceType().typeParametersValues().iterator(); var5.hasNext(); ++i) {
               ResolvedType tp = (ResolvedType)var5.next();
               this.matchTypeParameters(tp, (ResolvedType)actualType.asReferenceType().typeParametersValues().get(i), matchedTypeParameters);
            }
         }
      } else if (!expectedType.isPrimitive() && !expectedType.isWildcard()) {
         throw new UnsupportedOperationException(expectedType.getClass().getCanonicalName());
      }

   }

   private Optional<MethodUsage> solveMethodAsUsage(ResolvedTypeVariable tp, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext) {
      Iterator var6 = tp.asTypeParameter().getBounds().iterator();

      Optional methodUsage;
      do {
         if (!var6.hasNext()) {
            return Optional.empty();
         }

         ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)var6.next();
         methodUsage = this.solveMethodAsUsage(bound.getType(), name, argumentsTypes, typeSolver, invokationContext);
      } while(!methodUsage.isPresent());

      return methodUsage;
   }

   private Optional<MethodUsage> solveMethodAsUsage(ResolvedType type, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext) {
      if (type instanceof ResolvedReferenceType) {
         return this.solveMethodAsUsage((ResolvedReferenceType)type, name, argumentsTypes, typeSolver, invokationContext);
      } else if (type instanceof ResolvedTypeVariable) {
         return this.solveMethodAsUsage((ResolvedTypeVariable)type, name, argumentsTypes, typeSolver, invokationContext);
      } else if (type instanceof ResolvedWildcard) {
         ResolvedWildcard wildcardUsage = (ResolvedWildcard)type;
         if (wildcardUsage.isSuper()) {
            return this.solveMethodAsUsage(wildcardUsage.getBoundedType(), name, argumentsTypes, typeSolver, invokationContext);
         } else if (wildcardUsage.isExtends()) {
            throw new UnsupportedOperationException("extends wildcard");
         } else {
            throw new UnsupportedOperationException("unbounded wildcard");
         }
      } else if (type instanceof ResolvedLambdaConstraintType) {
         ResolvedLambdaConstraintType constraintType = (ResolvedLambdaConstraintType)type;
         return this.solveMethodAsUsage(constraintType.getBound(), name, argumentsTypes, typeSolver, invokationContext);
      } else if (type instanceof ResolvedArrayType) {
         return this.solveMethodAsUsage((ResolvedReferenceType)(new ReferenceTypeImpl(new ReflectionClassDeclaration(Object.class, typeSolver), typeSolver)), name, argumentsTypes, typeSolver, invokationContext);
      } else if (type instanceof ResolvedUnionType) {
         Optional<ResolvedReferenceType> commonAncestor = type.asUnionType().getCommonAncestor();
         if (commonAncestor.isPresent()) {
            return this.solveMethodAsUsage((ResolvedReferenceType)commonAncestor.get(), name, argumentsTypes, typeSolver, invokationContext);
         } else {
            throw new UnsupportedOperationException("no common ancestor available for " + type.describe());
         }
      } else {
         throw new UnsupportedOperationException("type usage: " + type.getClass().getCanonicalName());
      }
   }

   private ResolvedType usingParameterTypesFromScope(ResolvedType scope, ResolvedType type, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      if (type.isReferenceType()) {
         Iterator var4 = type.asReferenceType().getTypeParametersMap().iterator();

         while(var4.hasNext()) {
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> entry = (Pair)var4.next();
            if (((ResolvedTypeParameterDeclaration)entry.a).declaredOnType() && scope.asReferenceType().getGenericParameterByName(((ResolvedTypeParameterDeclaration)entry.a).getName()).isPresent()) {
               type = type.replaceTypeVariables((ResolvedTypeParameterDeclaration)entry.a, (ResolvedType)scope.asReferenceType().getGenericParameterByName(((ResolvedTypeParameterDeclaration)entry.a).getName()).get(), inferredTypes);
            }
         }

         return type;
      } else {
         return type;
      }
   }

   private ResolvedType applyInferredTypes(ResolvedType type, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      ResolvedTypeParameterDeclaration tp;
      for(Iterator var3 = inferredTypes.keySet().iterator(); var3.hasNext(); type = type.replaceTypeVariables(tp, (ResolvedType)inferredTypes.get(tp), inferredTypes)) {
         tp = (ResolvedTypeParameterDeclaration)var3.next();
      }

      return type;
   }
}
