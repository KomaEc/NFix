package com.github.javaparser.resolution.types.parametrization;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import java.util.Optional;

public interface ResolvedTypeParameterValueProvider {
   Optional<ResolvedType> typeParamValue(ResolvedTypeParameterDeclaration typeParameterDeclaration);

   default ResolvedType useThisTypeParametersOnTheGivenType(ResolvedType type) {
      if (type.isTypeVariable()) {
         ResolvedTypeParameterDeclaration typeParameter = type.asTypeParameter();
         if (typeParameter.declaredOnType()) {
            Optional<ResolvedType> typeParam = this.typeParamValue(typeParameter);
            if (typeParam.isPresent()) {
               type = (ResolvedType)typeParam.get();
            }
         }
      }

      if (type.isWildcard() && type.asWildcard().isBounded()) {
         return type.asWildcard().isExtends() ? ResolvedWildcard.extendsBound(this.useThisTypeParametersOnTheGivenType(type.asWildcard().getBoundedType())) : ResolvedWildcard.superBound(this.useThisTypeParametersOnTheGivenType(type.asWildcard().getBoundedType()));
      } else {
         if (type.isReferenceType()) {
            type = type.asReferenceType().transformTypeParameters(this::useThisTypeParametersOnTheGivenType);
         }

         return type;
      }
   }

   Optional<ResolvedType> getGenericParameterByName(String name);
}
