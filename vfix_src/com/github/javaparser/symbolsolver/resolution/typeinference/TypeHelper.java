package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedIntersectionType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.logic.FunctionalInterfaceLogic;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.utils.Pair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TypeHelper {
   public static boolean isProperType(ResolvedType type) {
      if (type instanceof InferenceVariable) {
         return false;
      } else if (type instanceof ResolvedReferenceType) {
         ResolvedReferenceType referenceType = (ResolvedReferenceType)type;
         return referenceType.typeParametersValues().stream().allMatch((it) -> {
            return isProperType(it);
         });
      } else if (type instanceof ResolvedWildcard) {
         ResolvedWildcard wildcard = (ResolvedWildcard)type;
         return wildcard.isBounded() ? isProperType(wildcard.getBoundedType()) : true;
      } else if (type.isPrimitive()) {
         return true;
      } else if (type.isTypeVariable()) {
         return false;
      } else if (type.isArray()) {
         return isProperType(type.asArrayType().getComponentType());
      } else {
         throw new UnsupportedOperationException(type.toString());
      }
   }

   public static boolean isCompatibleInAStrictInvocationContext(Expression expression, ResolvedType t) {
      throw new UnsupportedOperationException();
   }

   public static boolean isCompatibleInALooseInvocationContext(TypeSolver typeSolver, Expression expression, ResolvedType t) {
      return isCompatibleInALooseInvocationContext(JavaParserFacade.get(typeSolver).getType(expression), t);
   }

   public static boolean isCompatibleInALooseInvocationContext(ResolvedType s, ResolvedType t) {
      if (s.equals(t)) {
         return true;
      } else if (s.isPrimitive() && t.isPrimitive() && areCompatibleThroughWideningPrimitiveConversion(s, t)) {
         return true;
      } else if (s.isReferenceType() && t.isReferenceType() && areCompatibleThroughWideningReferenceConversion(s, t)) {
         return true;
      } else if (s.isPrimitive() && t.isReferenceType() && areCompatibleThroughWideningReferenceConversion(toBoxedType(s.asPrimitive()), t)) {
         return true;
      } else if (isUnboxable(s) && s.isReferenceType() && t.isPrimitive() && areCompatibleThroughWideningPrimitiveConversion(toUnboxedType(s.asReferenceType()), t)) {
         return true;
      } else {
         return s.isNull() && t.isReferenceType() ? true : t.isAssignableBy(s);
      }
   }

   private static boolean isUnboxable(ResolvedType referenceType) {
      return !referenceType.isReferenceType() ? false : Arrays.stream(ResolvedPrimitiveType.values()).anyMatch((pt) -> {
         return referenceType.asReferenceType().getQualifiedName().equals(pt.getBoxTypeQName());
      });
   }

   private static ResolvedType toUnboxedType(ResolvedReferenceType referenceType) {
      throw new UnsupportedOperationException(referenceType.toString());
   }

   private static ResolvedType toBoxedType(ResolvedPrimitiveType primitiveType) {
      throw new UnsupportedOperationException();
   }

   private static boolean areCompatibleThroughWideningReferenceConversion(ResolvedType s, ResolvedType t) {
      Optional<ResolvedPrimitiveType> correspondingPrimitiveTypeForS = Arrays.stream(ResolvedPrimitiveType.values()).filter((pt) -> {
         return pt.getBoxTypeQName().equals(s.asReferenceType().getQualifiedName());
      }).findFirst();
      if (!correspondingPrimitiveTypeForS.isPresent()) {
         return false;
      } else {
         throw new UnsupportedOperationException("areCompatibleThroughWideningReferenceConversion s=" + s + ", t=" + t);
      }
   }

   private static boolean areCompatibleThroughWideningPrimitiveConversion(ResolvedType s, ResolvedType t) {
      return s.isPrimitive() && t.isPrimitive() ? s.isAssignableBy(t) : false;
   }

   public static boolean isInferenceVariable(ResolvedType type) {
      return type instanceof InferenceVariable;
   }

   public static Set<InferenceVariable> usedInferenceVariables(ResolvedType type) {
      if (isInferenceVariable(type)) {
         return new HashSet(Arrays.asList((InferenceVariable)type));
      } else if (!type.isReferenceType()) {
         throw new UnsupportedOperationException(type.toString());
      } else {
         Set<InferenceVariable> res = new HashSet();
         Iterator var2 = type.asReferenceType().typeParametersValues().iterator();

         while(var2.hasNext()) {
            ResolvedType tp = (ResolvedType)var2.next();
            res.addAll(usedInferenceVariables(tp));
         }

         return res;
      }
   }

   public static ResolvedType leastUpperBound(Set<ResolvedType> types) {
      if (types.size() == 0) {
         throw new IllegalArgumentException();
      } else if (types.size() == 1) {
         return (ResolvedType)types.stream().findFirst().get();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public static Pair<ResolvedType, Boolean> groundTargetTypeOfLambda(LambdaExpr lambdaExpr, ResolvedType T, TypeSolver typeSolver) {
      boolean used18_5_3 = false;
      boolean wildcardParameterized = T.asReferenceType().typeParametersValues().stream().anyMatch((tp) -> {
         return tp.isWildcard();
      });
      if (wildcardParameterized) {
         if (ExpressionHelper.isExplicitlyTyped(lambdaExpr)) {
            used18_5_3 = true;
            throw new UnsupportedOperationException();
         } else {
            return new Pair(nonWildcardParameterizationOf(T.asReferenceType(), typeSolver), used18_5_3);
         }
      } else {
         return new Pair(T, used18_5_3);
      }
   }

   private static ResolvedReferenceType nonWildcardParameterizationOf(ResolvedReferenceType originalType, TypeSolver typeSolver) {
      List<ResolvedType> TIs = new LinkedList();
      List<ResolvedType> AIs = originalType.typeParametersValues();
      List<ResolvedTypeParameterDeclaration> TPs = originalType.getTypeDeclaration().getTypeParameters();
      ResolvedReferenceType object = new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver);

      for(int i = 0; i < AIs.size(); ++i) {
         ResolvedType Ai = (ResolvedType)AIs.get(i);
         ResolvedType Ti = null;
         if (!Ai.isWildcard()) {
            Ti = Ai;
         }

         if (Ti == null && Ai.isWildcard() && Ai.asWildcard().mention(originalType.getTypeDeclaration().getTypeParameters())) {
            throw new IllegalArgumentException();
         }

         if (Ti == null) {
            ResolvedType Bi = ((ResolvedTypeParameterDeclaration)TPs.get(i)).hasLowerBound() ? ((ResolvedTypeParameterDeclaration)TPs.get(i)).getLowerBound() : object;
            if (Ai.isWildcard() && !Ai.asWildcard().isBounded()) {
               Ti = Bi;
            } else if (Ai.isWildcard() && Ai.asWildcard().isUpperBounded()) {
               ResolvedType Ui = Ai.asWildcard().getBoundedType();
               Ti = glb(new HashSet(Arrays.asList(Ui, (ResolvedType)Bi)));
            } else {
               if (!Ai.isWildcard() || !Ai.asWildcard().isLowerBounded()) {
                  throw new RuntimeException("This should not happen");
               }

               Ti = Ai.asWildcard().getBoundedType();
            }
         }

         TIs.add(Ti);
      }

      return new ReferenceTypeImpl(originalType.getTypeDeclaration(), TIs, typeSolver);
   }

   public static MethodType getFunctionType(ResolvedType type) {
      Optional<MethodUsage> mu = FunctionalInterfaceLogic.getFunctionalMethod(type);
      if (mu.isPresent()) {
         return MethodType.fromMethodUsage((MethodUsage)mu.get());
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static ResolvedType glb(Set<ResolvedType> types) {
      if (types.size() == 0) {
         throw new IllegalArgumentException();
      } else {
         return (ResolvedType)(types.size() == 1 ? (ResolvedType)types.iterator().next() : new ResolvedIntersectionType(types));
      }
   }
}
