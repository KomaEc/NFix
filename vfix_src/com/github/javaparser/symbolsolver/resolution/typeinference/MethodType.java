package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.List;

public class MethodType {
   private List<ResolvedTypeParameterDeclaration> typeParameters;
   private List<ResolvedType> formalArgumentTypes;
   private ResolvedType returnType;
   private List<ResolvedType> exceptionTypes;

   public static MethodType fromMethodUsage(MethodUsage methodUsage) {
      return new MethodType(methodUsage.getDeclaration().getTypeParameters(), methodUsage.getParamTypes(), methodUsage.returnType(), methodUsage.exceptionTypes());
   }

   public MethodType(List<ResolvedTypeParameterDeclaration> typeParameters, List<ResolvedType> formalArgumentTypes, ResolvedType returnType, List<ResolvedType> exceptionTypes) {
      this.typeParameters = typeParameters;
      this.formalArgumentTypes = formalArgumentTypes;
      this.returnType = returnType;
      this.exceptionTypes = exceptionTypes;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.typeParameters;
   }

   public List<ResolvedType> getFormalArgumentTypes() {
      return this.formalArgumentTypes;
   }

   public ResolvedType getReturnType() {
      return this.returnType;
   }

   public List<ResolvedType> getExceptionTypes() {
      return this.exceptionTypes;
   }
}
