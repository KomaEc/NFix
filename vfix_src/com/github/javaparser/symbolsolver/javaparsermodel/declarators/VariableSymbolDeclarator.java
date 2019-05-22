package com.github.javaparser.symbolsolver.javaparsermodel.declarators;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class VariableSymbolDeclarator extends AbstractSymbolDeclarator<VariableDeclarationExpr> {
   public VariableSymbolDeclarator(VariableDeclarationExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
      wrappedNode.getParentNode().ifPresent((p) -> {
         if (p instanceof FieldDeclaration) {
            throw new IllegalArgumentException();
         }
      });
   }

   public List<ResolvedValueDeclaration> getSymbolDeclarations() {
      return (List)((VariableDeclarationExpr)this.wrappedNode).getVariables().stream().map((v) -> {
         return JavaParserSymbolDeclaration.localVar(v, this.typeSolver);
      }).collect(Collectors.toCollection(LinkedList::new));
   }
}
