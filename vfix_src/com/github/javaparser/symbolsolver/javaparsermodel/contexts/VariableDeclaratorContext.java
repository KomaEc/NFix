package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class VariableDeclaratorContext extends AbstractJavaParserContext<VariableDeclarator> {
   public VariableDeclaratorContext(VariableDeclarator wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      return ((VariableDeclarator)this.wrappedNode).getInitializer().isPresent() && ((VariableDeclarator)this.wrappedNode).getInitializer().get() == child ? Collections.singletonList(this.wrappedNode) : Collections.emptyList();
   }
}
