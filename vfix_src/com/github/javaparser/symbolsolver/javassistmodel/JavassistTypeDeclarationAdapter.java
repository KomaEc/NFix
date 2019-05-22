package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

public class JavassistTypeDeclarationAdapter {
   private CtClass ctClass;
   private TypeSolver typeSolver;

   public JavassistTypeDeclarationAdapter(CtClass ctClass, TypeSolver typeSolver) {
      this.ctClass = ctClass;
      this.typeSolver = typeSolver;
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return (Set)Arrays.stream(this.ctClass.getDeclaredMethods()).map((m) -> {
         return new JavassistMethodDeclaration(m, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return (List)Arrays.stream(this.ctClass.getConstructors()).map((m) -> {
         return new JavassistConstructorDeclaration(m, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public List<ResolvedFieldDeclaration> getDeclaredFields() {
      List<ResolvedFieldDeclaration> fieldDecls = new ArrayList();
      this.collectDeclaredFields(this.ctClass, fieldDecls);
      return fieldDecls;
   }

   private void collectDeclaredFields(CtClass ctClass, List<ResolvedFieldDeclaration> fieldDecls) {
      if (ctClass != null) {
         Arrays.stream(ctClass.getDeclaredFields()).forEach((f) -> {
            fieldDecls.add(new JavassistFieldDeclaration(f, this.typeSolver));
         });

         try {
            this.collectDeclaredFields(ctClass.getSuperclass(), fieldDecls);
         } catch (NotFoundException var4) {
         }
      }

   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      if (null == this.ctClass.getGenericSignature()) {
         return Collections.emptyList();
      } else {
         try {
            SignatureAttribute.ClassSignature classSignature = SignatureAttribute.toClassSignature(this.ctClass.getGenericSignature());
            return (List)Arrays.stream(classSignature.getParameters()).map((tp) -> {
               return new JavassistTypeParameter(tp, JavassistFactory.toTypeDeclaration(this.ctClass, this.typeSolver), this.typeSolver);
            }).collect(Collectors.toList());
         } catch (BadBytecode var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      try {
         return this.ctClass.getDeclaringClass() == null ? Optional.empty() : Optional.of(JavassistFactory.toTypeDeclaration(this.ctClass.getDeclaringClass(), this.typeSolver));
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }
}
