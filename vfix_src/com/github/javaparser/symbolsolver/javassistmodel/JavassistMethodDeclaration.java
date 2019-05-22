package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
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
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

public class JavassistMethodDeclaration implements ResolvedMethodDeclaration {
   private CtMethod ctMethod;
   private TypeSolver typeSolver;

   public JavassistMethodDeclaration(CtMethod ctMethod, TypeSolver typeSolver) {
      this.ctMethod = ctMethod;
      this.typeSolver = typeSolver;
   }

   public boolean isDefaultMethod() {
      return this.ctMethod.getDeclaringClass().isInterface() && !this.isAbstract();
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.ctMethod.getModifiers());
   }

   public String toString() {
      return "JavassistMethodDeclaration{ctMethod=" + this.ctMethod + '}';
   }

   public String getName() {
      return this.ctMethod.getName();
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

   public ResolvedReferenceTypeDeclaration declaringType() {
      if (this.ctMethod.getDeclaringClass().isInterface()) {
         return new JavassistInterfaceDeclaration(this.ctMethod.getDeclaringClass(), this.typeSolver);
      } else {
         return (ResolvedReferenceTypeDeclaration)(this.ctMethod.getDeclaringClass().isEnum() ? new JavassistEnumDeclaration(this.ctMethod.getDeclaringClass(), this.typeSolver) : new JavassistClassDeclaration(this.ctMethod.getDeclaringClass(), this.typeSolver));
      }
   }

   public ResolvedType getReturnType() {
      try {
         if (this.ctMethod.getGenericSignature() != null) {
            SignatureAttribute.Type genericSignatureType = SignatureAttribute.toMethodSignature(this.ctMethod.getGenericSignature()).getReturnType();
            return JavassistUtils.signatureTypeToType(genericSignatureType, this.typeSolver, this);
         } else {
            return JavassistFactory.typeUsageFor(this.ctMethod.getReturnType(), this.typeSolver);
         }
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      } catch (BadBytecode var3) {
         throw new RuntimeException(var3);
      }
   }

   public int getNumberOfParams() {
      try {
         return this.ctMethod.getParameterTypes().length;
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResolvedParameterDeclaration getParam(int i) {
      try {
         boolean variadic = false;
         if ((this.ctMethod.getModifiers() & 128) > 0) {
            variadic = i == this.ctMethod.getParameterTypes().length - 1;
         }

         Optional<String> paramName = JavassistUtils.extractParameterName(this.ctMethod, i);
         if (this.ctMethod.getGenericSignature() != null) {
            SignatureAttribute.MethodSignature methodSignature = SignatureAttribute.toMethodSignature(this.ctMethod.getGenericSignature());
            SignatureAttribute.Type signatureType = methodSignature.getParameterTypes()[i];
            return new JavassistParameterDeclaration(JavassistUtils.signatureTypeToType(signatureType, this.typeSolver, this), this.typeSolver, variadic, (String)paramName.orElse((Object)null));
         } else {
            return new JavassistParameterDeclaration(this.ctMethod.getParameterTypes()[i], this.typeSolver, variadic, (String)paramName.orElse((Object)null));
         }
      } catch (NotFoundException var6) {
         throw new RuntimeException(var6);
      } catch (BadBytecode var7) {
         throw new RuntimeException(var7);
      }
   }

   public MethodUsage getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
      return (new MethodDeclarationCommonLogic(this, this.typeSolver)).resolveTypeVariables(context, parameterTypes);
   }

   public boolean isAbstract() {
      return Modifier.isAbstract(this.ctMethod.getModifiers());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      try {
         if (this.ctMethod.getGenericSignature() == null) {
            return Collections.emptyList();
         } else {
            SignatureAttribute.MethodSignature methodSignature = SignatureAttribute.toMethodSignature(this.ctMethod.getGenericSignature());
            return (List)Arrays.stream(methodSignature.getTypeParameters()).map((jasTp) -> {
               return new JavassistTypeParameter(jasTp, this, this.typeSolver);
            }).collect(Collectors.toList());
         }
      } catch (BadBytecode var2) {
         throw new RuntimeException(var2);
      }
   }

   public AccessSpecifier accessSpecifier() {
      return JavassistFactory.modifiersToAccessLevel(this.ctMethod.getModifiers());
   }

   public int getNumberOfSpecifiedExceptions() {
      try {
         return this.ctMethod.getExceptionTypes().length;
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResolvedType getSpecifiedException(int index) {
      if (index >= 0 && index < this.getNumberOfSpecifiedExceptions()) {
         try {
            return JavassistFactory.typeUsageFor(this.ctMethod.getExceptionTypes()[index], this.typeSolver);
         } catch (NotFoundException var3) {
            throw new RuntimeException(var3);
         }
      } else {
         throw new IllegalArgumentException(String.format("No exception with index %d. Number of exceptions: %d", index, this.getNumberOfSpecifiedExceptions()));
      }
   }

   public Optional<MethodDeclaration> toAst() {
      return Optional.empty();
   }
}
