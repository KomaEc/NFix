package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.LambdaExprContext;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import java.util.Optional;

public class JavaParserParameterDeclaration implements ResolvedParameterDeclaration {
   private Parameter wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserParameterDeclaration(Parameter wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return true;
   }

   public boolean isVariadic() {
      return this.wrappedNode.isVarArgs();
   }

   public boolean isType() {
      throw new UnsupportedOperationException();
   }

   public ResolvedType getType() {
      if (this.wrappedNode.getType() instanceof UnknownType && JavaParserFactory.getContext(this.wrappedNode, this.typeSolver) instanceof LambdaExprContext) {
         Optional<Value> value = JavaParserFactory.getContext(this.wrappedNode, this.typeSolver).solveSymbolAsValue(this.wrappedNode.getNameAsString(), this.typeSolver);
         if (value.isPresent()) {
            return ((Value)value.get()).getType();
         }
      }

      ResolvedType res = JavaParserFacade.get(this.typeSolver).convert(this.wrappedNode.getType(), (Node)this.wrappedNode);
      if (this.isVariadic()) {
         res = new ResolvedArrayType((ResolvedType)res);
      }

      return (ResolvedType)res;
   }

   public ResolvedParameterDeclaration asParameter() {
      return this;
   }

   public Parameter getWrappedNode() {
      return this.wrappedNode;
   }
}
