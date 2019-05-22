package com.github.javaparser.symbolsolver.core.resolution;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.AbstractJavaParserContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Context {
   Context getParent();

   default Optional<ResolvedType> solveGenericType(String name, TypeSolver typeSolver) {
      return Optional.empty();
   }

   default SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      Context parent = this.getParent();
      return parent == null ? SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class) : parent.solveType(name, typeSolver);
   }

   default SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      return this.getParent().solveSymbol(name, typeSolver);
   }

   default Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      SymbolReference<? extends ResolvedValueDeclaration> ref = this.solveSymbol(name, typeSolver);
      if (ref.isSolved()) {
         Value value = Value.from((ResolvedValueDeclaration)ref.getCorrespondingDeclaration());
         return Optional.of(value);
      } else {
         return Optional.empty();
      }
   }

   default List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      return Collections.emptyList();
   }

   default List<Parameter> parametersExposedToChild(Node child) {
      return Collections.emptyList();
   }

   default List<ResolvedFieldDeclaration> fieldsExposedToChild(Node child) {
      return Collections.emptyList();
   }

   default Optional<VariableDeclarator> localVariableDeclarationInScope(String name) {
      if (this.getParent() == null) {
         return Optional.empty();
      } else {
         Optional<VariableDeclarator> localRes = this.getParent().localVariablesExposedToChild(((AbstractJavaParserContext)this).getWrappedNode()).stream().filter((vd) -> {
            return vd.getNameAsString().equals(name);
         }).findFirst();
         return localRes.isPresent() ? localRes : this.getParent().localVariableDeclarationInScope(name);
      }
   }

   default Optional<Parameter> parameterDeclarationInScope(String name) {
      if (this.getParent() == null) {
         return Optional.empty();
      } else {
         Optional<Parameter> localRes = this.getParent().parametersExposedToChild(((AbstractJavaParserContext)this).getWrappedNode()).stream().filter((vd) -> {
            return vd.getNameAsString().equals(name);
         }).findFirst();
         return localRes.isPresent() ? localRes : this.getParent().parameterDeclarationInScope(name);
      }
   }

   default Optional<ResolvedFieldDeclaration> fieldDeclarationInScope(String name) {
      if (this.getParent() == null) {
         return Optional.empty();
      } else {
         Optional<ResolvedFieldDeclaration> localRes = this.getParent().fieldsExposedToChild(((AbstractJavaParserContext)this).getWrappedNode()).stream().filter((vd) -> {
            return vd.getName().equals(name);
         }).findFirst();
         return localRes.isPresent() ? localRes : this.getParent().fieldDeclarationInScope(name);
      }
   }

   default SymbolReference<ResolvedConstructorDeclaration> solveConstructor(List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      throw new IllegalArgumentException("Constructor resolution is available only on Class Context");
   }

   default SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, staticOnly, typeSolver);
   }

   default Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      SymbolReference<ResolvedMethodDeclaration> methodSolved = this.solveMethod(name, argumentsTypes, false, typeSolver);
      if (methodSolved.isSolved()) {
         ResolvedMethodDeclaration methodDeclaration = (ResolvedMethodDeclaration)methodSolved.getCorrespondingDeclaration();
         MethodUsage methodUsage = ContextHelper.resolveTypeVariables(this, methodDeclaration, argumentsTypes);
         return Optional.of(methodUsage);
      } else {
         return Optional.empty();
      }
   }
}
