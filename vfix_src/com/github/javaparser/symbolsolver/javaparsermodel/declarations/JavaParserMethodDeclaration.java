package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.declarations.common.MethodDeclarationCommonLogic;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaParserMethodDeclaration implements ResolvedMethodDeclaration {
   private MethodDeclaration wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserMethodDeclaration(MethodDeclaration wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public String toString() {
      return "JavaParserMethodDeclaration{wrappedNode=" + this.wrappedNode + ", typeSolver=" + this.typeSolver + '}';
   }

   public ResolvedReferenceTypeDeclaration declaringType() {
      if (Navigator.requireParentNode(this.wrappedNode) instanceof ObjectCreationExpr) {
         ObjectCreationExpr parentNode = (ObjectCreationExpr)Navigator.requireParentNode(this.wrappedNode);
         return new JavaParserAnonymousClassDeclaration(parentNode, this.typeSolver);
      } else {
         return JavaParserFactory.toTypeDeclaration(Navigator.requireParentNode(this.wrappedNode), this.typeSolver);
      }
   }

   public ResolvedType getReturnType() {
      return JavaParserFacade.get(this.typeSolver).convert(this.wrappedNode.getType(), this.getContext());
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

   public MethodUsage getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
      return (new MethodDeclarationCommonLogic(this, this.typeSolver)).resolveTypeVariables(context, parameterTypes);
   }

   private Context getContext() {
      return JavaParserFactory.getContext(this.wrappedNode, this.typeSolver);
   }

   public boolean isAbstract() {
      return !this.wrappedNode.getBody().isPresent();
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public boolean isField() {
      throw new UnsupportedOperationException();
   }

   public boolean isParameter() {
      throw new UnsupportedOperationException();
   }

   public boolean isType() {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return (List)this.wrappedNode.getTypeParameters().stream().map((astTp) -> {
         return new JavaParserTypeParameter(astTp, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public boolean isDefaultMethod() {
      return this.wrappedNode.isDefault();
   }

   public boolean isStatic() {
      return this.wrappedNode.isStatic();
   }

   public MethodDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public AccessSpecifier accessSpecifier() {
      return AstResolutionUtils.toAccessLevel(this.wrappedNode.getModifiers());
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

   public Optional<MethodDeclaration> toAst() {
      return Optional.of(this.wrappedNode);
   }
}
