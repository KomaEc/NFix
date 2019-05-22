package com.github.javaparser.resolution;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametersMap;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametrized;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MethodUsage implements ResolvedTypeParametrized {
   private ResolvedMethodDeclaration declaration;
   private List<ResolvedType> paramTypes;
   private List<ResolvedType> exceptionTypes;
   private ResolvedType returnType;
   private ResolvedTypeParametersMap typeParametersMap;

   public MethodUsage(ResolvedMethodDeclaration declaration) {
      this.paramTypes = new ArrayList();
      this.exceptionTypes = new ArrayList();
      this.typeParametersMap = ResolvedTypeParametersMap.empty();
      this.declaration = declaration;

      int i;
      for(i = 0; i < declaration.getNumberOfParams(); ++i) {
         this.paramTypes.add(declaration.getParam(i).getType());
      }

      for(i = 0; i < declaration.getNumberOfSpecifiedExceptions(); ++i) {
         this.exceptionTypes.add(declaration.getSpecifiedException(i));
      }

      this.returnType = declaration.getReturnType();
   }

   public MethodUsage(ResolvedMethodDeclaration declaration, List<ResolvedType> paramTypes, ResolvedType returnType) {
      this(declaration, paramTypes, returnType, declaration.getSpecifiedExceptions(), ResolvedTypeParametersMap.empty());
   }

   public MethodUsage(ResolvedMethodDeclaration declaration, List<ResolvedType> paramTypes, ResolvedType returnType, List<ResolvedType> exceptionTypes) {
      this(declaration, paramTypes, returnType, exceptionTypes, ResolvedTypeParametersMap.empty());
   }

   private MethodUsage(ResolvedMethodDeclaration declaration, List<ResolvedType> paramTypes, ResolvedType returnType, List<ResolvedType> exceptionTypes, ResolvedTypeParametersMap typeParametersMap) {
      this.paramTypes = new ArrayList();
      this.exceptionTypes = new ArrayList();
      this.declaration = declaration;
      this.paramTypes = paramTypes;
      this.returnType = returnType;
      this.exceptionTypes = exceptionTypes;
      this.typeParametersMap = typeParametersMap;
   }

   public String toString() {
      return "MethodUsage{declaration=" + this.declaration + ", paramTypes=" + this.paramTypes + '}';
   }

   public ResolvedMethodDeclaration getDeclaration() {
      return this.declaration;
   }

   public String getName() {
      return this.declaration.getName();
   }

   public ResolvedReferenceTypeDeclaration declaringType() {
      return this.declaration.declaringType();
   }

   public ResolvedType returnType() {
      return this.returnType;
   }

   public List<ResolvedType> getParamTypes() {
      return this.paramTypes;
   }

   public MethodUsage replaceParamType(int i, ResolvedType replaced) {
      if (i >= 0 && i < this.getNoParams()) {
         if (this.paramTypes.get(i) == replaced) {
            return this;
         } else {
            List<ResolvedType> newParams = new LinkedList(this.paramTypes);
            newParams.set(i, replaced);
            return new MethodUsage(this.declaration, newParams, this.returnType, this.exceptionTypes, this.typeParametersMap);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public MethodUsage replaceExceptionType(int i, ResolvedType replaced) {
      if (i >= 0 && i < this.exceptionTypes.size()) {
         if (this.exceptionTypes.get(i) == replaced) {
            return this;
         } else {
            List<ResolvedType> newTypes = new LinkedList(this.exceptionTypes);
            newTypes.set(i, replaced);
            return new MethodUsage(this.declaration, this.paramTypes, this.returnType, newTypes, this.typeParametersMap);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public MethodUsage replaceReturnType(ResolvedType returnType) {
      return returnType == this.returnType ? this : new MethodUsage(this.declaration, this.paramTypes, returnType, this.exceptionTypes, this.typeParametersMap);
   }

   public int getNoParams() {
      return this.paramTypes.size();
   }

   public ResolvedType getParamType(int i) {
      return (ResolvedType)this.paramTypes.get(i);
   }

   public MethodUsage replaceTypeParameter(ResolvedTypeParameterDeclaration typeParameter, ResolvedType type) {
      if (type == null) {
         throw new IllegalArgumentException();
      } else {
         MethodUsage res = new MethodUsage(this.declaration, this.paramTypes, this.returnType, this.exceptionTypes, this.typeParametersMap.toBuilder().setValue(typeParameter, type).build());
         Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes = new HashMap();

         int i;
         ResolvedType newReturnType;
         ResolvedType newType;
         for(i = 0; i < this.paramTypes.size(); ++i) {
            newReturnType = (ResolvedType)this.paramTypes.get(i);
            newType = newReturnType.replaceTypeVariables(typeParameter, type, inferredTypes);
            res = res.replaceParamType(i, newType);
         }

         for(i = 0; i < this.exceptionTypes.size(); ++i) {
            newReturnType = (ResolvedType)this.exceptionTypes.get(i);
            newType = newReturnType.replaceTypeVariables(typeParameter, type, inferredTypes);
            res = res.replaceExceptionType(i, newType);
         }

         ResolvedType oldReturnType = res.returnType;
         newReturnType = oldReturnType.replaceTypeVariables(typeParameter, type, inferredTypes);
         res = res.replaceReturnType(newReturnType);
         return res;
      }
   }

   public ResolvedTypeParametersMap typeParametersMap() {
      return this.typeParametersMap;
   }

   public String getQualifiedSignature() {
      return this.getDeclaration().getQualifiedSignature();
   }

   public List<ResolvedType> exceptionTypes() {
      return this.exceptionTypes;
   }
}
