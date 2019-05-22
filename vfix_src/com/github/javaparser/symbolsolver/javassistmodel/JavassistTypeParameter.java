package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.declarations.ResolvedMethodLikeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParametrizable;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javassist.bytecode.SignatureAttribute;

public class JavassistTypeParameter implements ResolvedTypeParameterDeclaration {
   private SignatureAttribute.TypeParameter wrapped;
   private TypeSolver typeSolver;
   private ResolvedTypeParametrizable container;

   public JavassistTypeParameter(SignatureAttribute.TypeParameter wrapped, ResolvedTypeParametrizable container, TypeSolver typeSolver) {
      this.wrapped = wrapped;
      this.typeSolver = typeSolver;
      this.container = container;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ResolvedTypeParameterDeclaration)) {
         return false;
      } else {
         ResolvedTypeParameterDeclaration that = (ResolvedTypeParameterDeclaration)o;
         if (!this.getQualifiedName().equals(that.getQualifiedName())) {
            return false;
         } else if (this.declaredOnType() != that.declaredOnType()) {
            return false;
         } else {
            return this.declaredOnMethod() == that.declaredOnMethod();
         }
      }
   }

   public String toString() {
      return "JavassistTypeParameter{" + this.wrapped.getName() + '}';
   }

   public String getName() {
      return this.wrapped.getName();
   }

   public String getContainerQualifiedName() {
      if (this.container instanceof ResolvedReferenceTypeDeclaration) {
         return ((ResolvedReferenceTypeDeclaration)this.container).getQualifiedName();
      } else if (this.container instanceof ResolvedMethodLikeDeclaration) {
         return ((ResolvedMethodLikeDeclaration)this.container).getQualifiedName();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public String getContainerId() {
      return this.getContainerQualifiedName();
   }

   public ResolvedTypeParametrizable getContainer() {
      return this.container;
   }

   public List<ResolvedTypeParameterDeclaration.Bound> getBounds() {
      List<ResolvedTypeParameterDeclaration.Bound> bounds = new ArrayList();
      if (this.wrapped.getClassBound() != null && !this.wrapped.getClassBound().toString().equals(Object.class.getCanonicalName())) {
         throw new UnsupportedOperationException(this.wrapped.getClassBound().toString());
      } else {
         SignatureAttribute.ObjectType[] var2 = this.wrapped.getInterfaceBound();
         int var3 = var2.length;
         byte var4 = 0;
         if (var4 < var3) {
            SignatureAttribute.ObjectType ot = var2[var4];
            throw new UnsupportedOperationException(ot.toString());
         } else {
            return bounds;
         }
      }
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.container instanceof ResolvedReferenceTypeDeclaration ? Optional.of((ResolvedReferenceTypeDeclaration)this.container) : Optional.empty();
   }
}
