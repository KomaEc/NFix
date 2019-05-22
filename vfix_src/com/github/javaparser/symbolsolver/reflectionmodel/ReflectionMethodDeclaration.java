package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.declarations.common.MethodDeclarationCommonLogic;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionMethodDeclaration implements ResolvedMethodDeclaration {
   private Method method;
   private TypeSolver typeSolver;

   public ReflectionMethodDeclaration(Method method, TypeSolver typeSolver) {
      this.method = method;
      if (!method.isSynthetic() && !method.isBridge()) {
         this.typeSolver = typeSolver;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getName() {
      return this.method.getName();
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return false;
   }

   public String toString() {
      return "ReflectionMethodDeclaration{method=" + this.method + '}';
   }

   public boolean isType() {
      return false;
   }

   public ResolvedReferenceTypeDeclaration declaringType() {
      if (this.method.getDeclaringClass().isInterface()) {
         return new ReflectionInterfaceDeclaration(this.method.getDeclaringClass(), this.typeSolver);
      } else {
         return (ResolvedReferenceTypeDeclaration)(this.method.getDeclaringClass().isEnum() ? new ReflectionEnumDeclaration(this.method.getDeclaringClass(), this.typeSolver) : new ReflectionClassDeclaration(this.method.getDeclaringClass(), this.typeSolver));
      }
   }

   public ResolvedType getReturnType() {
      return ReflectionFactory.typeUsageFor(this.method.getGenericReturnType(), this.typeSolver);
   }

   public int getNumberOfParams() {
      return this.method.getParameterTypes().length;
   }

   public ResolvedParameterDeclaration getParam(int i) {
      boolean variadic = false;
      if (this.method.isVarArgs()) {
         variadic = i == this.method.getParameterCount() - 1;
      }

      return new ReflectionParameterDeclaration(this.method.getParameterTypes()[i], this.method.getGenericParameterTypes()[i], this.typeSolver, variadic, this.method.getParameters()[i].getName());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return (List)Arrays.stream(this.method.getTypeParameters()).map((refTp) -> {
         return new ReflectionTypeParameter(refTp, false, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
      return (new MethodDeclarationCommonLogic(this, this.typeSolver)).resolveTypeVariables(context, parameterTypes);
   }

   public boolean isAbstract() {
      return Modifier.isAbstract(this.method.getModifiers());
   }

   public boolean isDefaultMethod() {
      return this.method.isDefault();
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.method.getModifiers());
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.method.getModifiers());
   }

   public int getNumberOfSpecifiedExceptions() {
      return this.method.getExceptionTypes().length;
   }

   public ResolvedType getSpecifiedException(int index) {
      if (index >= 0 && index < this.getNumberOfSpecifiedExceptions()) {
         return ReflectionFactory.typeUsageFor(this.method.getExceptionTypes()[index], this.typeSolver);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Optional<MethodDeclaration> toAst() {
      return Optional.empty();
   }
}
