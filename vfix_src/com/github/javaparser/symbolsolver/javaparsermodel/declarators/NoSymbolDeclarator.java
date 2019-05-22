package com.github.javaparser.symbolsolver.javaparsermodel.declarators;

import com.github.javaparser.ast.Node;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class NoSymbolDeclarator<N extends Node> extends AbstractSymbolDeclarator<N> {
   public NoSymbolDeclarator(N wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<ResolvedValueDeclaration> getSymbolDeclarations() {
      return Collections.emptyList();
   }
}
