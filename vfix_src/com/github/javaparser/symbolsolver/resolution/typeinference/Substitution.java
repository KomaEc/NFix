package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.LinkedList;
import java.util.List;

public class Substitution {
   private List<ResolvedTypeParameterDeclaration> typeParameterDeclarations = new LinkedList();
   private List<ResolvedType> types = new LinkedList();
   private static final Substitution EMPTY = new Substitution();

   public static Substitution empty() {
      return EMPTY;
   }

   public Substitution withPair(ResolvedTypeParameterDeclaration typeParameterDeclaration, ResolvedType type) {
      Substitution newInstance = new Substitution();
      newInstance.typeParameterDeclarations.addAll(this.typeParameterDeclarations);
      newInstance.types.addAll(this.types);
      newInstance.typeParameterDeclarations.add(typeParameterDeclaration);
      newInstance.types.add(type);
      return newInstance;
   }

   private Substitution() {
   }

   public ResolvedType apply(ResolvedType originalType) {
      ResolvedType result = originalType;

      for(int i = 0; i < this.typeParameterDeclarations.size(); ++i) {
         result = result.replaceTypeVariables((ResolvedTypeParameterDeclaration)this.typeParameterDeclarations.get(i), (ResolvedType)this.types.get(i));
      }

      return result;
   }
}
