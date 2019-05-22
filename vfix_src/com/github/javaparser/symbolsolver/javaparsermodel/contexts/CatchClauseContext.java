package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CatchClauseContext extends AbstractJavaParserContext<CatchClause> {
   public CatchClauseContext(CatchClause wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public final SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(((CatchClause)this.wrappedNode).getParameter(), typeSolver);
      SymbolReference<? extends ResolvedValueDeclaration> symbolReference = AbstractJavaParserContext.solveWith(sb, name);
      return symbolReference.isSolved() ? symbolReference : this.getParent().solveSymbol(name, typeSolver);
   }

   public final Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(((CatchClause)this.wrappedNode).getParameter(), typeSolver);
      Optional<Value> symbolReference = this.solveWithAsValue(sb, name, typeSolver);
      return symbolReference.isPresent() ? symbolReference : this.getParent().solveSymbolAsValue(name, typeSolver);
   }

   public final SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      return Collections.emptyList();
   }

   public List<Parameter> parametersExposedToChild(Node child) {
      return child == ((CatchClause)this.getWrappedNode()).getBody() ? Collections.singletonList(((CatchClause)this.getWrappedNode()).getParameter()) : Collections.emptyList();
   }
}
