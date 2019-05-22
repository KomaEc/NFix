package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParametrizable;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Modifier;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

public class JavassistFieldDeclaration implements ResolvedFieldDeclaration {
   private CtField ctField;
   private TypeSolver typeSolver;

   public JavassistFieldDeclaration(CtField ctField, TypeSolver typeSolver) {
      this.ctField = ctField;
      this.typeSolver = typeSolver;
   }

   public ResolvedType getType() {
      try {
         if (this.ctField.getGenericSignature() != null && this.declaringType() instanceof ResolvedTypeParametrizable) {
            SignatureAttribute.Type genericSignatureType = SignatureAttribute.toFieldSignature(this.ctField.getGenericSignature());
            return JavassistUtils.signatureTypeToType(genericSignatureType, this.typeSolver, (ResolvedTypeParametrizable)this.declaringType());
         } else {
            return JavassistFactory.typeUsageFor(this.ctField.getType(), this.typeSolver);
         }
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      } catch (BadBytecode var3) {
         throw new RuntimeException(var3);
      }
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.ctField.getModifiers());
   }

   public String getName() {
      return this.ctField.getName();
   }

   public boolean isField() {
      return true;
   }

   public boolean isParameter() {
      return false;
   }

   public boolean isType() {
      return false;
   }

   public AccessSpecifier accessSpecifier() {
      return JavassistFactory.modifiersToAccessLevel(this.ctField.getModifiers());
   }

   public ResolvedTypeDeclaration declaringType() {
      return JavassistFactory.toTypeDeclaration(this.ctField.getDeclaringClass(), this.typeSolver);
   }
}
