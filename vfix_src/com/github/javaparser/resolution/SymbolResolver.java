package com.github.javaparser.resolution;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

public interface SymbolResolver {
   <T> T resolveDeclaration(Node node, Class<T> resultClass);

   <T> T toResolvedType(Type javaparserType, Class<T> resultClass);

   ResolvedType calculateType(Expression expression);
}
