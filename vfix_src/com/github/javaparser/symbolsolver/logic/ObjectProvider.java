package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.types.ResolvedReferenceType;

public interface ObjectProvider {
   ResolvedReferenceType object();

   ResolvedReferenceType byName(String var1);
}
