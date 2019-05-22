package com.github.javaparser.resolution.types.parametrization;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResolvedTypeParametersMap {
   private Map<String, ResolvedType> nameToValue;
   private Map<String, ResolvedTypeParameterDeclaration> nameToDeclaration;

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ResolvedTypeParametersMap)) {
         return false;
      } else {
         ResolvedTypeParametersMap that = (ResolvedTypeParametersMap)o;
         return this.nameToValue.equals(that.nameToValue) && this.nameToDeclaration.equals(that.nameToDeclaration);
      }
   }

   public int hashCode() {
      return this.nameToValue.hashCode();
   }

   public String toString() {
      return "TypeParametersMap{nameToValue=" + this.nameToValue + '}';
   }

   public static ResolvedTypeParametersMap empty() {
      return (new ResolvedTypeParametersMap.Builder()).build();
   }

   private ResolvedTypeParametersMap(Map<String, ResolvedType> nameToValue, Map<String, ResolvedTypeParameterDeclaration> nameToDeclaration) {
      this.nameToValue = new HashMap();
      this.nameToValue.putAll(nameToValue);
      this.nameToDeclaration = new HashMap();
      this.nameToDeclaration.putAll(nameToDeclaration);
   }

   public ResolvedType getValue(ResolvedTypeParameterDeclaration typeParameter) {
      String qualifiedName = typeParameter.getQualifiedName();
      return (ResolvedType)(this.nameToValue.containsKey(qualifiedName) ? (ResolvedType)this.nameToValue.get(qualifiedName) : new ResolvedTypeVariable(typeParameter));
   }

   public Optional<ResolvedType> getValueBySignature(String signature) {
      return this.nameToValue.containsKey(signature) ? Optional.of(this.nameToValue.get(signature)) : Optional.empty();
   }

   public List<String> getNames() {
      return new ArrayList(this.nameToValue.keySet());
   }

   public List<ResolvedType> getTypes() {
      return new ArrayList(this.nameToValue.values());
   }

   public ResolvedTypeParametersMap.Builder toBuilder() {
      return new ResolvedTypeParametersMap.Builder(this.nameToValue, this.nameToDeclaration);
   }

   public boolean isEmpty() {
      return this.nameToValue.isEmpty();
   }

   public ResolvedType replaceAll(ResolvedType type) {
      Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes = new HashMap();

      ResolvedTypeParameterDeclaration typeParameterDeclaration;
      for(Iterator var3 = this.nameToDeclaration.values().iterator(); var3.hasNext(); type = type.replaceTypeVariables(typeParameterDeclaration, this.getValue(typeParameterDeclaration), inferredTypes)) {
         typeParameterDeclaration = (ResolvedTypeParameterDeclaration)var3.next();
      }

      return type;
   }

   // $FF: synthetic method
   ResolvedTypeParametersMap(Map x0, Map x1, Object x2) {
      this(x0, x1);
   }

   public static class Builder {
      private Map<String, ResolvedType> nameToValue;
      private Map<String, ResolvedTypeParameterDeclaration> nameToDeclaration;

      public Builder() {
         this.nameToValue = new HashMap();
         this.nameToDeclaration = new HashMap();
      }

      private Builder(Map<String, ResolvedType> nameToValue, Map<String, ResolvedTypeParameterDeclaration> nameToDeclaration) {
         this.nameToValue = new HashMap();
         this.nameToValue.putAll(nameToValue);
         this.nameToDeclaration = new HashMap();
         this.nameToDeclaration.putAll(nameToDeclaration);
      }

      public ResolvedTypeParametersMap build() {
         return new ResolvedTypeParametersMap(this.nameToValue, this.nameToDeclaration);
      }

      public ResolvedTypeParametersMap.Builder setValue(ResolvedTypeParameterDeclaration typeParameter, ResolvedType value) {
         String qualifiedName = typeParameter.getQualifiedName();
         this.nameToValue.put(qualifiedName, value);
         this.nameToDeclaration.put(qualifiedName, typeParameter);
         return this;
      }

      // $FF: synthetic method
      Builder(Map x0, Map x1, Object x2) {
         this(x0, x1);
      }
   }
}
