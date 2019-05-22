package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import java.util.List;

public interface SymbolDeclarator {
   List<ResolvedValueDeclaration> getSymbolDeclarations();
}
