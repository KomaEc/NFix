package com.github.javaparser.symbolsolver.javaparsermodel.declarators;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FieldSymbolDeclarator extends AbstractSymbolDeclarator<FieldDeclaration> {
   public FieldSymbolDeclarator(FieldDeclaration wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<ResolvedValueDeclaration> getSymbolDeclarations() {
      List<ResolvedValueDeclaration> symbols = new LinkedList();
      Iterator var2 = ((FieldDeclaration)this.wrappedNode).getVariables().iterator();

      while(var2.hasNext()) {
         VariableDeclarator v = (VariableDeclarator)var2.next();
         symbols.add(JavaParserSymbolDeclaration.field(v, this.typeSolver));
      }

      return symbols;
   }
}
