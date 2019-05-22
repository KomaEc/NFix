package com.github.javaparser.symbolsolver.javaparsermodel.declarators;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.LinkedList;
import java.util.List;

public class ParameterSymbolDeclarator extends AbstractSymbolDeclarator<Parameter> {
   public ParameterSymbolDeclarator(Parameter wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<ResolvedValueDeclaration> getSymbolDeclarations() {
      List<ResolvedValueDeclaration> symbols = new LinkedList();
      symbols.add(JavaParserSymbolDeclaration.parameter((Parameter)this.wrappedNode, this.typeSolver));
      return symbols;
   }
}
