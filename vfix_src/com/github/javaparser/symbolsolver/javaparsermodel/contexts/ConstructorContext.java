package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class ConstructorContext extends AbstractMethodLikeDeclarationContext<ConstructorDeclaration> {
   public ConstructorContext(ConstructorDeclaration wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<Parameter> parametersExposedToChild(Node child) {
      return (List)(child == ((ConstructorDeclaration)this.wrappedNode).getBody() ? ((ConstructorDeclaration)this.wrappedNode).getParameters() : Collections.emptyList());
   }
}
