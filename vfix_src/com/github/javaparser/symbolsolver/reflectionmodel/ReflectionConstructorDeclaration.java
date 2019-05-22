package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionConstructorDeclaration implements ResolvedConstructorDeclaration {
   private Constructor<?> constructor;
   private TypeSolver typeSolver;

   public ReflectionConstructorDeclaration(Constructor<?> constructor, TypeSolver typeSolver) {
      this.constructor = constructor;
      this.typeSolver = typeSolver;
   }

   public ResolvedClassDeclaration declaringType() {
      return new ReflectionClassDeclaration(this.constructor.getDeclaringClass(), this.typeSolver);
   }

   public int getNumberOfParams() {
      return this.constructor.getParameterCount();
   }

   public ResolvedParameterDeclaration getParam(int i) {
      if (i >= 0 && i < this.getNumberOfParams()) {
         boolean variadic = false;
         if (this.constructor.isVarArgs()) {
            variadic = i == this.constructor.getParameterCount() - 1;
         }

         return new ReflectionParameterDeclaration(this.constructor.getParameterTypes()[i], this.constructor.getGenericParameterTypes()[i], this.typeSolver, variadic, this.constructor.getParameters()[i].getName());
      } else {
         throw new IllegalArgumentException(String.format("No param with index %d. Number of params: %d", i, this.getNumberOfParams()));
      }
   }

   public String getName() {
      return this.constructor.getDeclaringClass().getSimpleName();
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.constructor.getModifiers());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return (List)Arrays.stream(this.constructor.getTypeParameters()).map((refTp) -> {
         return new ReflectionTypeParameter(refTp, false, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public int getNumberOfSpecifiedExceptions() {
      return this.constructor.getExceptionTypes().length;
   }

   public ResolvedType getSpecifiedException(int index) {
      if (index >= 0 && index < this.getNumberOfSpecifiedExceptions()) {
         return ReflectionFactory.typeUsageFor(this.constructor.getExceptionTypes()[index], this.typeSolver);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Optional<ConstructorDeclaration> toAst() {
      return Optional.empty();
   }
}
