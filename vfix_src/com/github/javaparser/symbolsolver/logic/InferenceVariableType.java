package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class InferenceVariableType implements ResolvedType {
   private int id;
   private ResolvedTypeParameterDeclaration correspondingTp;
   private Set<ResolvedType> equivalentTypes = new HashSet();
   private ObjectProvider objectProvider;
   private Set<ResolvedType> superTypes = new HashSet();

   public String toString() {
      return "InferenceVariableType{id=" + this.id + '}';
   }

   public void setCorrespondingTp(ResolvedTypeParameterDeclaration correspondingTp) {
      this.correspondingTp = correspondingTp;
   }

   public void registerEquivalentType(ResolvedType type) {
      this.equivalentTypes.add(type);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof InferenceVariableType)) {
         return false;
      } else {
         InferenceVariableType that = (InferenceVariableType)o;
         return this.id == that.id;
      }
   }

   public int hashCode() {
      return this.id;
   }

   public InferenceVariableType(int id, ObjectProvider objectProvider) {
      this.id = id;
      this.objectProvider = objectProvider;
   }

   public static InferenceVariableType fromWildcard(ResolvedWildcard wildcard, int id, ObjectProvider objectProvider) {
      InferenceVariableType inferenceVariableType = new InferenceVariableType(id, objectProvider);
      if (wildcard.isExtends()) {
         inferenceVariableType.superTypes.add(wildcard.getBoundedType());
      }

      if (wildcard.isSuper()) {
         inferenceVariableType.superTypes.add(wildcard.getBoundedType());
      }

      return inferenceVariableType;
   }

   public String describe() {
      return "InferenceVariable_" + this.id;
   }

   public boolean isAssignableBy(ResolvedType other) {
      throw new UnsupportedOperationException();
   }

   private Set<ResolvedType> concreteEquivalentTypesAlsoIndirectly(Set<InferenceVariableType> considered, InferenceVariableType inferenceVariableType) {
      considered.add(inferenceVariableType);
      Set<ResolvedType> result = new HashSet();
      result.addAll((Collection)inferenceVariableType.equivalentTypes.stream().filter((t) -> {
         return !t.isTypeVariable() && !(t instanceof InferenceVariableType);
      }).collect(Collectors.toSet()));
      inferenceVariableType.equivalentTypes.stream().filter((t) -> {
         return t instanceof InferenceVariableType;
      }).forEach((t) -> {
         InferenceVariableType ivt = (InferenceVariableType)t;
         if (!considered.contains(ivt)) {
            result.addAll(this.concreteEquivalentTypesAlsoIndirectly(considered, ivt));
         }

      });
      return result;
   }

   public ResolvedType equivalentType() {
      Set<ResolvedType> concreteEquivalent = this.concreteEquivalentTypesAlsoIndirectly(new HashSet(), this);
      if (concreteEquivalent.isEmpty()) {
         return (ResolvedType)(this.correspondingTp == null ? this.objectProvider.object() : new ResolvedTypeVariable(this.correspondingTp));
      } else if (concreteEquivalent.size() == 1) {
         return (ResolvedType)concreteEquivalent.iterator().next();
      } else {
         Set<ResolvedType> notTypeVariables = (Set)this.equivalentTypes.stream().filter((t) -> {
            return !t.isTypeVariable() && !this.hasInferenceVariables(t);
         }).collect(Collectors.toSet());
         if (notTypeVariables.size() == 1) {
            return (ResolvedType)notTypeVariables.iterator().next();
         } else if (notTypeVariables.size() == 0 && !this.superTypes.isEmpty()) {
            if (this.superTypes.size() == 1) {
               return (ResolvedType)this.superTypes.iterator().next();
            } else {
               throw new IllegalStateException("Super types are: " + this.superTypes);
            }
         } else {
            throw new IllegalStateException("Equivalent types are: " + this.equivalentTypes);
         }
      }
   }

   private boolean hasInferenceVariables(ResolvedType type) {
      if (type instanceof InferenceVariableType) {
         return true;
      } else if (type.isReferenceType()) {
         ResolvedReferenceType refType = type.asReferenceType();
         Iterator var3 = refType.typeParametersValues().iterator();

         ResolvedType t;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            t = (ResolvedType)var3.next();
         } while(!this.hasInferenceVariables(t));

         return true;
      } else if (type.isWildcard()) {
         ResolvedWildcard wildcardType = type.asWildcard();
         return this.hasInferenceVariables(wildcardType.getBoundedType());
      } else {
         return false;
      }
   }
}
