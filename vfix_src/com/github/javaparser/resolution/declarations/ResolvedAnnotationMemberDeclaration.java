package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.expr.Expression;

public interface ResolvedAnnotationMemberDeclaration extends ResolvedValueDeclaration {
   Expression getDefaultValue();
}
