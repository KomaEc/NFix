package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedLambdaConstraintType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InferenceContext {
   private int nextInferenceVariableId = 0;
   private ObjectProvider objectProvider;
   private List<InferenceVariableType> inferenceVariableTypes = new ArrayList();
   private Map<String, InferenceVariableType> inferenceVariableTypeMap = new HashMap();

   public InferenceContext(ObjectProvider objectProvider) {
      this.objectProvider = objectProvider;
   }

   private InferenceVariableType inferenceVariableTypeForTp(ResolvedTypeParameterDeclaration tp) {
      if (!this.inferenceVariableTypeMap.containsKey(tp.getName())) {
         InferenceVariableType inferenceVariableType = new InferenceVariableType(this.nextInferenceVariableId++, this.objectProvider);
         this.inferenceVariableTypes.add(inferenceVariableType);
         inferenceVariableType.setCorrespondingTp(tp);
         this.inferenceVariableTypeMap.put(tp.getName(), inferenceVariableType);
      }

      return (InferenceVariableType)this.inferenceVariableTypeMap.get(tp.getName());
   }

   public ResolvedType addPair(ResolvedType target, ResolvedType actual) {
      target = this.placeInferenceVariables(target);
      actual = this.placeInferenceVariables(actual);
      this.registerCorrespondance(target, actual);
      return target;
   }

   public ResolvedType addSingle(ResolvedType actual) {
      return this.placeInferenceVariables(actual);
   }

   private void registerCorrespondance(ResolvedType formalType, ResolvedType actualType) {
      if (formalType.isReferenceType() && actualType.isReferenceType()) {
         ResolvedReferenceType formalTypeAsReference = formalType.asReferenceType();
         ResolvedReferenceType actualTypeAsReference = actualType.asReferenceType();
         if (!formalTypeAsReference.getQualifiedName().equals(actualTypeAsReference.getQualifiedName())) {
            List<ResolvedReferenceType> ancestors = actualTypeAsReference.getAllAncestors();
            String formalParamTypeQName = formalTypeAsReference.getQualifiedName();
            List<ResolvedType> correspondingFormalType = (List)ancestors.stream().filter((a) -> {
               return a.getQualifiedName().equals(formalParamTypeQName);
            }).collect(Collectors.toList());
            if (correspondingFormalType.isEmpty()) {
               ancestors = formalTypeAsReference.getAllAncestors();
               String actualParamTypeQname = actualTypeAsReference.getQualifiedName();
               List<ResolvedType> correspondingActualType = (List)ancestors.stream().filter((a) -> {
                  return a.getQualifiedName().equals(actualParamTypeQname);
               }).collect(Collectors.toList());
               if (correspondingActualType.isEmpty()) {
                  throw new ConfilictingGenericTypesException(formalType, actualType);
               }

               correspondingFormalType = correspondingActualType;
            }

            actualTypeAsReference = ((ResolvedType)correspondingFormalType.get(0)).asReferenceType();
         }

         if (formalTypeAsReference.getQualifiedName().equals(actualTypeAsReference.getQualifiedName()) && !formalTypeAsReference.typeParametersValues().isEmpty() && !actualTypeAsReference.isRawType()) {
            int i = 0;

            for(Iterator var14 = formalTypeAsReference.typeParametersValues().iterator(); var14.hasNext(); ++i) {
               ResolvedType formalTypeParameter = (ResolvedType)var14.next();
               this.registerCorrespondance(formalTypeParameter, (ResolvedType)actualTypeAsReference.typeParametersValues().get(i));
            }
         }
      } else if (formalType instanceof InferenceVariableType && !actualType.isPrimitive()) {
         ((InferenceVariableType)formalType).registerEquivalentType(actualType);
         if (actualType instanceof InferenceVariableType) {
            ((InferenceVariableType)actualType).registerEquivalentType(formalType);
         }
      } else if (!actualType.isNull() && !actualType.equals(formalType)) {
         if (actualType.isArray() && formalType.isArray()) {
            this.registerCorrespondance(formalType.asArrayType().getComponentType(), actualType.asArrayType().getComponentType());
         } else if (formalType.isWildcard()) {
            if (actualType instanceof InferenceVariableType && formalType.asWildcard().isBounded()) {
               ((InferenceVariableType)actualType).registerEquivalentType(formalType.asWildcard().getBoundedType());
               if (formalType.asWildcard().getBoundedType() instanceof InferenceVariableType) {
                  ((InferenceVariableType)formalType.asWildcard().getBoundedType()).registerEquivalentType(actualType);
               }
            }

            if (actualType.isWildcard()) {
               ResolvedWildcard formalWildcard = formalType.asWildcard();
               ResolvedWildcard actualWildcard = actualType.asWildcard();
               if (formalWildcard.isBounded() && formalWildcard.getBoundedType() instanceof InferenceVariableType) {
                  if (formalWildcard.isSuper() && actualWildcard.isSuper()) {
                     ((InferenceVariableType)formalType.asWildcard().getBoundedType()).registerEquivalentType(actualWildcard.getBoundedType());
                  } else if (formalWildcard.isExtends() && actualWildcard.isExtends()) {
                     ((InferenceVariableType)formalType.asWildcard().getBoundedType()).registerEquivalentType(actualWildcard.getBoundedType());
                  }
               }
            }

            if (actualType.isReferenceType() && formalType.asWildcard().isBounded()) {
               this.registerCorrespondance(formalType.asWildcard().getBoundedType(), actualType);
            }
         } else if (actualType instanceof InferenceVariableType) {
            if (formalType instanceof ResolvedReferenceType) {
               ((InferenceVariableType)actualType).registerEquivalentType(formalType);
            } else if (formalType instanceof InferenceVariableType) {
               ((InferenceVariableType)actualType).registerEquivalentType(formalType);
            }
         } else if (actualType.isConstraint()) {
            ResolvedLambdaConstraintType constraintType = actualType.asConstraintType();
            if (constraintType.getBound() instanceof InferenceVariableType) {
               ((InferenceVariableType)constraintType.getBound()).registerEquivalentType(formalType);
            }
         } else {
            if (!actualType.isPrimitive()) {
               throw new UnsupportedOperationException(formalType.describe() + " " + actualType.describe());
            }

            if (!formalType.isPrimitive()) {
               this.registerCorrespondance(formalType, this.objectProvider.byName(actualType.asPrimitive().getBoxTypeQName()));
            }
         }
      }

   }

   private ResolvedType placeInferenceVariables(ResolvedType type) {
      if (type.isWildcard()) {
         if (type.asWildcard().isExtends()) {
            return ResolvedWildcard.extendsBound(this.placeInferenceVariables(type.asWildcard().getBoundedType()));
         } else {
            return (ResolvedType)(type.asWildcard().isSuper() ? ResolvedWildcard.superBound(this.placeInferenceVariables(type.asWildcard().getBoundedType())) : type);
         }
      } else if (type.isTypeVariable()) {
         return this.inferenceVariableTypeForTp(type.asTypeParameter());
      } else if (type.isReferenceType()) {
         return type.asReferenceType().transformTypeParameters((tp) -> {
            return this.placeInferenceVariables(tp);
         });
      } else if (type.isArray()) {
         return new ResolvedArrayType(this.placeInferenceVariables(type.asArrayType().getComponentType()));
      } else if (!type.isNull() && !type.isPrimitive() && !type.isVoid()) {
         if (type.isConstraint()) {
            return ResolvedLambdaConstraintType.bound(this.placeInferenceVariables(type.asConstraintType().getBound()));
         } else if (type instanceof InferenceVariableType) {
            return type;
         } else {
            throw new UnsupportedOperationException(type.describe());
         }
      } else {
         return type;
      }
   }

   public ResolvedType resolve(ResolvedType type) {
      if (type instanceof InferenceVariableType) {
         InferenceVariableType inferenceVariableType = (InferenceVariableType)type;
         return inferenceVariableType.equivalentType();
      } else if (type.isReferenceType()) {
         return type.asReferenceType().transformTypeParameters((tp) -> {
            return this.resolve(tp);
         });
      } else if (!type.isNull() && !type.isPrimitive() && !type.isVoid()) {
         if (type.isArray()) {
            return new ResolvedArrayType(this.resolve(type.asArrayType().getComponentType()));
         } else if (type.isWildcard()) {
            if (type.asWildcard().isExtends()) {
               return ResolvedWildcard.extendsBound(this.resolve(type.asWildcard().getBoundedType()));
            } else {
               return (ResolvedType)(type.asWildcard().isSuper() ? ResolvedWildcard.superBound(this.resolve(type.asWildcard().getBoundedType())) : type);
            }
         } else {
            throw new UnsupportedOperationException(type.describe());
         }
      } else {
         return type;
      }
   }
}
