package com.github.javaparser.symbolsolver.core.resolution;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistMethodDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionMethodDeclaration;
import java.util.List;

class ContextHelper {
   private ContextHelper() {
   }

   static MethodUsage resolveTypeVariables(Context context, ResolvedMethodDeclaration methodDeclaration, List<ResolvedType> parameterTypes) {
      if (methodDeclaration instanceof JavaParserMethodDeclaration) {
         return ((JavaParserMethodDeclaration)methodDeclaration).resolveTypeVariables(context, parameterTypes);
      } else if (methodDeclaration instanceof JavassistMethodDeclaration) {
         return ((JavassistMethodDeclaration)methodDeclaration).resolveTypeVariables(context, parameterTypes);
      } else if (methodDeclaration instanceof JavaParserEnumDeclaration.ValuesMethod) {
         return ((JavaParserEnumDeclaration.ValuesMethod)methodDeclaration).resolveTypeVariables(context, parameterTypes);
      } else if (methodDeclaration instanceof ReflectionMethodDeclaration) {
         return ((ReflectionMethodDeclaration)methodDeclaration).resolveTypeVariables(context, parameterTypes);
      } else {
         throw new UnsupportedOperationException();
      }
   }
}
