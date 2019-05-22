package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class MethodContext extends AbstractMethodLikeDeclarationContext<MethodDeclaration> {
   public MethodContext(MethodDeclaration wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<Parameter> parametersExposedToChild(Node child) {
      return (List)(((MethodDeclaration)this.wrappedNode).getBody().isPresent() && child == ((MethodDeclaration)this.wrappedNode).getBody().get() ? ((MethodDeclaration)this.wrappedNode).getParameters() : Collections.emptyList());
   }
}
