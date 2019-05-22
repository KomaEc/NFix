package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

public interface ResolvedMethodDeclaration extends ResolvedMethodLikeDeclaration, AssociableToAST<MethodDeclaration> {
   ResolvedType getReturnType();

   boolean isAbstract();

   boolean isDefaultMethod();

   boolean isStatic();
}
