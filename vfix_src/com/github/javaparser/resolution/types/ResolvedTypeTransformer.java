package com.github.javaparser.resolution.types;

@FunctionalInterface
public interface ResolvedTypeTransformer {
   ResolvedType transform(ResolvedType type);
}
