package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Iterator;
import java.util.List;

public class ObjectCreationContext extends AbstractJavaParserContext<ObjectCreationExpr> {
   public ObjectCreationContext(ObjectCreationExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      if (((ObjectCreationExpr)this.wrappedNode).getScope().isPresent()) {
         Expression scope = (Expression)((ObjectCreationExpr)this.wrappedNode).getScope().get();
         ResolvedType scopeType = JavaParserFacade.get(typeSolver).getType(scope);
         if (scopeType.isReferenceType()) {
            ResolvedReferenceType referenceType = scopeType.asReferenceType();
            Iterator var6 = referenceType.getTypeDeclaration().internalTypes().iterator();

            while(var6.hasNext()) {
               ResolvedTypeDeclaration it = (ResolvedTypeDeclaration)var6.next();
               if (it.getName().equals(name)) {
                  return SymbolReference.solved(it);
               }
            }
         }

         throw new UnsolvedSymbolException("Unable to solve qualified object creation expression in the context of expression of type " + scopeType.describe());
      } else {
         Node parentNode;
         for(parentNode = Navigator.requireParentNode(this.wrappedNode); parentNode instanceof ObjectCreationExpr; parentNode = Navigator.requireParentNode(parentNode)) {
         }

         return JavaParserFactory.getContext(parentNode, typeSolver).solveType(name, typeSolver);
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      return JavaParserFactory.getContext(Navigator.requireParentNode(this.wrappedNode), typeSolver).solveSymbol(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return JavaParserFactory.getContext(Navigator.requireParentNode(this.wrappedNode), typeSolver).solveMethod(name, argumentsTypes, false, typeSolver);
   }
}
