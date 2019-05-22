package com.github.javaparser.resolution.declarations;

import com.github.javaparser.resolution.types.ResolvedType;

public interface ResolvedValueDeclaration extends ResolvedDeclaration {
   ResolvedType getType();
}
