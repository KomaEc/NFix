package com.github.javaparser.symbolsolver.declarations.common;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.logic.InferenceContext;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.reflectionmodel.MyObjectProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MethodDeclarationCommonLogic {
   private ResolvedMethodDeclaration methodDeclaration;
   private TypeSolver typeSolver;

   public MethodDeclarationCommonLogic(ResolvedMethodDeclaration methodDeclaration, TypeSolver typeSolver) {
      this.methodDeclaration = methodDeclaration;
      this.typeSolver = typeSolver;
   }

   public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
      ResolvedType returnType = this.replaceTypeParams(this.methodDeclaration.getReturnType(), this.typeSolver, context);
      List<ResolvedType> params = new ArrayList();

      for(int i = 0; i < this.methodDeclaration.getNumberOfParams(); ++i) {
         ResolvedType replaced = this.replaceTypeParams(this.methodDeclaration.getParam(i).getType(), this.typeSolver, context);
         params.add(replaced);
      }

      InferenceContext inferenceContext = new InferenceContext(MyObjectProvider.INSTANCE);

      for(int i = 0; i < this.methodDeclaration.getNumberOfParams() - (this.methodDeclaration.hasVariadicParameter() ? 1 : 0); ++i) {
         ResolvedType formalParamType = this.methodDeclaration.getParam(i).getType();
         ResolvedType actualParamType = (ResolvedType)parameterTypes.get(i);
         inferenceContext.addPair(formalParamType, actualParamType);
      }

      returnType = inferenceContext.resolve(inferenceContext.addSingle(returnType));
      return new MethodUsage(this.methodDeclaration, params, returnType);
   }

   private ResolvedType replaceTypeParams(ResolvedType type, TypeSolver typeSolver, Context context) {
      if (type.isTypeVariable()) {
         ResolvedTypeParameterDeclaration typeParameter = type.asTypeParameter();
         if (typeParameter.declaredOnType()) {
            Optional<ResolvedType> typeParam = this.typeParamByName(typeParameter.getName(), typeSolver, context);
            if (typeParam.isPresent()) {
               type = (ResolvedType)typeParam.get();
            }
         }
      }

      if (type.isReferenceType()) {
         type.asReferenceType().transformTypeParameters((tp) -> {
            return this.replaceTypeParams(tp, typeSolver, context);
         });
      }

      return type;
   }

   protected Optional<ResolvedType> typeParamByName(String name, TypeSolver typeSolver, Context context) {
      return this.methodDeclaration.getTypeParameters().stream().filter((tp) -> {
         return tp.getName().equals(name);
      }).map((tp) -> {
         return this.toType(tp);
      }).findFirst();
   }

   protected ResolvedType toType(ResolvedTypeParameterDeclaration typeParameterDeclaration) {
      return new ResolvedTypeVariable(typeParameterDeclaration);
   }
}
