package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.body.ConstructorDeclaration;

public interface ResolvedConstructorDeclaration extends ResolvedMethodLikeDeclaration, AssociableToAST<ConstructorDeclaration> {
   ResolvedReferenceTypeDeclaration declaringType();
}
