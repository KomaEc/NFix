package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeParameter;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMethodLikeDeclarationContext<T extends Node & NodeWithParameters<T> & NodeWithTypeParameters<T>> extends AbstractJavaParserContext<T> {
   public AbstractMethodLikeDeclarationContext(T wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public final SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Iterator var3 = ((NodeWithParameters)this.wrappedNode).getParameters().iterator();

      SymbolReference symbolReference;
      do {
         if (!var3.hasNext()) {
            return this.getParent().solveSymbol(name, typeSolver);
         }

         Parameter parameter = (Parameter)var3.next();
         SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(parameter, typeSolver);
         symbolReference = AbstractJavaParserContext.solveWith(sb, name);
      } while(!symbolReference.isSolved());

      return symbolReference;
   }

   public final Optional<ResolvedType> solveGenericType(String name, TypeSolver typeSolver) {
      Iterator var3 = ((NodeWithTypeParameters)this.wrappedNode).getTypeParameters().iterator();

      TypeParameter tp;
      do {
         if (!var3.hasNext()) {
            return super.solveGenericType(name, typeSolver);
         }

         tp = (TypeParameter)var3.next();
      } while(!tp.getName().getId().equals(name));

      return Optional.of(new ResolvedTypeVariable(new JavaParserTypeParameter(tp, typeSolver)));
   }

   public final Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      Iterator var3 = ((NodeWithParameters)this.wrappedNode).getParameters().iterator();

      Optional symbolReference;
      do {
         if (!var3.hasNext()) {
            return this.getParent().solveSymbolAsValue(name, typeSolver);
         }

         Parameter parameter = (Parameter)var3.next();
         SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(parameter, typeSolver);
         symbolReference = this.solveWithAsValue(sb, name, typeSolver);
      } while(!symbolReference.isPresent());

      return symbolReference;
   }

   public final SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      if (((NodeWithTypeParameters)this.wrappedNode).getTypeParameters() != null) {
         Iterator var3 = ((NodeWithTypeParameters)this.wrappedNode).getTypeParameters().iterator();

         while(var3.hasNext()) {
            TypeParameter tp = (TypeParameter)var3.next();
            if (tp.getName().getId().equals(name)) {
               return SymbolReference.solved(new JavaParserTypeParameter(tp, typeSolver));
            }
         }
      }

      List<TypeDeclaration> localTypes = this.wrappedNode.findAll(TypeDeclaration.class);
      Iterator var7 = localTypes.iterator();

      TypeDeclaration localType;
      do {
         if (!var7.hasNext()) {
            return this.getParent().solveType(name, typeSolver);
         }

         localType = (TypeDeclaration)var7.next();
         if (localType.getName().getId().equals(name)) {
            return SymbolReference.solved(JavaParserFacade.get(typeSolver).getTypeDeclaration(localType));
         }
      } while(!name.startsWith(String.format("%s.", localType.getName())));

      return JavaParserFactory.getContext(localType, typeSolver).solveType(name.substring(localType.getName().getId().length() + 1), typeSolver);
   }

   public final SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }
}
