package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import java.util.List;

public interface ResolvedAnnotationDeclaration extends ResolvedReferenceTypeDeclaration, AssociableToAST<AnnotationDeclaration> {
   List<ResolvedAnnotationMemberDeclaration> getAnnotationMembers();
}
