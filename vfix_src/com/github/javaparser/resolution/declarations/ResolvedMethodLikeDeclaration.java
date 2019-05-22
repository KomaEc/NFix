package com.github.javaparser.resolution.declarations;

import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface ResolvedMethodLikeDeclaration extends ResolvedDeclaration, ResolvedTypeParametrizable, HasAccessSpecifier {
   default String getPackageName() {
      return this.declaringType().getPackageName();
   }

   default String getClassName() {
      return this.declaringType().getClassName();
   }

   default String getQualifiedName() {
      return this.declaringType().getQualifiedName() + "." + this.getName();
   }

   default String getSignature() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.getName());
      sb.append("(");

      for(int i = 0; i < this.getNumberOfParams(); ++i) {
         if (i != 0) {
            sb.append(", ");
         }

         sb.append(this.getParam(i).describeType());
      }

      sb.append(")");
      return sb.toString();
   }

   default String getQualifiedSignature() {
      return this.declaringType().getId() + "." + this.getSignature();
   }

   ResolvedReferenceTypeDeclaration declaringType();

   int getNumberOfParams();

   ResolvedParameterDeclaration getParam(int i);

   default ResolvedParameterDeclaration getLastParam() {
      if (this.getNumberOfParams() == 0) {
         throw new UnsupportedOperationException("This method has no typeParametersValues, therefore it has no a last parameter");
      } else {
         return this.getParam(this.getNumberOfParams() - 1);
      }
   }

   default boolean hasVariadicParameter() {
      return this.getNumberOfParams() == 0 ? false : this.getParam(this.getNumberOfParams() - 1).isVariadic();
   }

   default Optional<ResolvedTypeParameterDeclaration> findTypeParameter(String name) {
      Iterator var2 = this.getTypeParameters().iterator();

      ResolvedTypeParameterDeclaration tp;
      do {
         if (!var2.hasNext()) {
            return this.declaringType().findTypeParameter(name);
         }

         tp = (ResolvedTypeParameterDeclaration)var2.next();
      } while(!tp.getName().equals(name));

      return Optional.of(tp);
   }

   int getNumberOfSpecifiedExceptions();

   ResolvedType getSpecifiedException(int index);

   default List<ResolvedType> getSpecifiedExceptions() {
      if (this.getNumberOfSpecifiedExceptions() == 0) {
         return Collections.emptyList();
      } else {
         List<ResolvedType> exceptions = new LinkedList();

         for(int i = 0; i < this.getNumberOfSpecifiedExceptions(); ++i) {
            exceptions.add(this.getSpecifiedException(i));
         }

         return exceptions;
      }
   }
}
