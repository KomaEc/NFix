package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaParserConstructorDeclaration<N extends ResolvedReferenceTypeDeclaration> implements ResolvedConstructorDeclaration {
   private N declaringType;
   private ConstructorDeclaration wrappedNode;
   private TypeSolver typeSolver;

   JavaParserConstructorDeclaration(N declaringType, ConstructorDeclaration wrappedNode, TypeSolver typeSolver) {
      this.declaringType = declaringType;
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public N declaringType() {
      return this.declaringType;
   }

   public int getNumberOfParams() {
      return this.wrappedNode.getParameters().size();
   }

   public ResolvedParameterDeclaration getParam(int i) {
      if (i >= 0 && i < this.getNumberOfParams()) {
         return new JavaParserParameterDeclaration((Parameter)this.wrappedNode.getParameters().get(i), this.typeSolver);
      } else {
         throw new IllegalArgumentException(String.format("No param with index %d. Number of params: %d", i, this.getNumberOfParams()));
      }
   }

   public String getName() {
      return this.declaringType.getName();
   }

   public ConstructorDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public AccessSpecifier accessSpecifier() {
      return AstResolutionUtils.toAccessLevel(this.wrappedNode.getModifiers());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return (List)this.wrappedNode.getTypeParameters().stream().map((astTp) -> {
         return new JavaParserTypeParameter(astTp, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public int getNumberOfSpecifiedExceptions() {
      return this.wrappedNode.getThrownExceptions().size();
   }

   public ResolvedType getSpecifiedException(int index) {
      if (index >= 0 && index < this.getNumberOfSpecifiedExceptions()) {
         return JavaParserFacade.get(this.typeSolver).convert((Type)this.wrappedNode.getThrownExceptions().get(index), (Node)this.wrappedNode);
      } else {
         throw new IllegalArgumentException(String.format("No exception with index %d. Number of exceptions: %d", index, this.getNumberOfSpecifiedExceptions()));
      }
   }

   public Optional<ConstructorDeclaration> toAst() {
      return Optional.of(this.wrappedNode);
   }
}
