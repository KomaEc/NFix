package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

public class JavassistConstructorDeclaration implements ResolvedConstructorDeclaration {
   private CtConstructor ctConstructor;
   private TypeSolver typeSolver;

   public JavassistConstructorDeclaration(CtConstructor ctConstructor, TypeSolver typeSolver) {
      this.ctConstructor = ctConstructor;
      this.typeSolver = typeSolver;
   }

   public String toString() {
      return "JavassistMethodDeclaration{CtConstructor=" + this.ctConstructor + '}';
   }

   public String getName() {
      return this.ctConstructor.getName();
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return false;
   }

   public boolean isType() {
      return false;
   }

   public ResolvedClassDeclaration declaringType() {
      return new JavassistClassDeclaration(this.ctConstructor.getDeclaringClass(), this.typeSolver);
   }

   public int getNumberOfParams() {
      try {
         return this.ctConstructor.getParameterTypes().length;
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResolvedParameterDeclaration getParam(int i) {
      try {
         boolean variadic = false;
         if ((this.ctConstructor.getModifiers() & 128) > 0) {
            variadic = i == this.ctConstructor.getParameterTypes().length - 1;
         }

         Optional<String> paramName = JavassistUtils.extractParameterName(this.ctConstructor, i);
         if (this.ctConstructor.getGenericSignature() != null) {
            SignatureAttribute.MethodSignature methodSignature = SignatureAttribute.toMethodSignature(this.ctConstructor.getGenericSignature());
            SignatureAttribute.Type signatureType = methodSignature.getParameterTypes()[i];
            return new JavassistParameterDeclaration(JavassistUtils.signatureTypeToType(signatureType, this.typeSolver, this), this.typeSolver, variadic, (String)paramName.orElse((Object)null));
         } else {
            return new JavassistParameterDeclaration(this.ctConstructor.getParameterTypes()[i], this.typeSolver, variadic, (String)paramName.orElse((Object)null));
         }
      } catch (NotFoundException var6) {
         throw new RuntimeException(var6);
      } catch (BadBytecode var7) {
         throw new RuntimeException(var7);
      }
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      try {
         if (this.ctConstructor.getGenericSignature() == null) {
            return Collections.emptyList();
         } else {
            SignatureAttribute.MethodSignature methodSignature = SignatureAttribute.toMethodSignature(this.ctConstructor.getGenericSignature());
            return (List)Arrays.stream(methodSignature.getTypeParameters()).map((jasTp) -> {
               return new JavassistTypeParameter(jasTp, this, this.typeSolver);
            }).collect(Collectors.toList());
         }
      } catch (BadBytecode var2) {
         throw new RuntimeException(var2);
      }
   }

   public AccessSpecifier accessSpecifier() {
      return JavassistFactory.modifiersToAccessLevel(this.ctConstructor.getModifiers());
   }

   public int getNumberOfSpecifiedExceptions() {
      try {
         return this.ctConstructor.getExceptionTypes().length;
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResolvedType getSpecifiedException(int index) {
      if (index >= 0 && index < this.getNumberOfSpecifiedExceptions()) {
         try {
            return JavassistFactory.typeUsageFor(this.ctConstructor.getExceptionTypes()[index], this.typeSolver);
         } catch (NotFoundException var3) {
            throw new RuntimeException(var3);
         }
      } else {
         throw new IllegalArgumentException(String.format("No exception with index %d. Number of exceptions: %d", index, this.getNumberOfSpecifiedExceptions()));
      }
   }

   public Optional<ConstructorDeclaration> toAst() {
      return Optional.empty();
   }
}
