package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class FieldAccessContext extends AbstractJavaParserContext<FieldAccessExpr> {
   private static final String ARRAY_LENGTH_FIELD_NAME = "length";

   public FieldAccessContext(FieldAccessExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      if (((FieldAccessExpr)this.wrappedNode).getName().toString().equals(name) && ((FieldAccessExpr)this.wrappedNode).getScope() instanceof ThisExpr) {
         ResolvedType typeOfThis = JavaParserFacade.get(typeSolver).getTypeOfThisIn(this.wrappedNode);
         return (new SymbolSolver(typeSolver)).solveSymbolInType(typeOfThis.asReferenceType().getTypeDeclaration(), name);
      } else {
         return JavaParserFactory.getContext(Navigator.requireParentNode(this.wrappedNode), typeSolver).solveSymbol(name, typeSolver);
      }
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      return JavaParserFactory.getContext(Navigator.requireParentNode(this.wrappedNode), typeSolver).solveType(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes, boolean staticOnly, TypeSolver typeSolver) {
      return JavaParserFactory.getContext(Navigator.requireParentNode(this.wrappedNode), typeSolver).solveMethod(name, parameterTypes, false, typeSolver);
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      Expression scope = ((FieldAccessExpr)this.wrappedNode).getScope();
      if (((FieldAccessExpr)this.wrappedNode).getName().toString().equals(name)) {
         ResolvedType typeOfScope = JavaParserFacade.get(typeSolver).getType(scope);
         if (typeOfScope.isArray() && name.equals("length")) {
            return Optional.of(new Value(ResolvedPrimitiveType.INT, "length"));
         } else if (typeOfScope.isReferenceType()) {
            if (typeOfScope.asReferenceType().getTypeDeclaration().isEnum()) {
               ResolvedEnumDeclaration enumDeclaration = (ResolvedEnumDeclaration)typeOfScope.asReferenceType().getTypeDeclaration();
               if (enumDeclaration.hasEnumConstant(name)) {
                  return Optional.of(new Value(enumDeclaration.getEnumConstant(name).getType(), name));
               }
            }

            Optional<ResolvedType> typeUsage = typeOfScope.asReferenceType().getFieldType(name);
            return typeUsage.map((resolvedType) -> {
               return new Value(resolvedType, name);
            });
         } else {
            return Optional.empty();
         }
      } else {
         return this.getParent().solveSymbolAsValue(name, typeSolver);
      }
   }

   public SymbolReference<ResolvedValueDeclaration> solveField(String name, TypeSolver typeSolver) {
      Collection<ResolvedReferenceTypeDeclaration> rrtds = this.findTypeDeclarations(Optional.of(((FieldAccessExpr)this.wrappedNode).getScope()), typeSolver);
      Iterator var4 = rrtds.iterator();

      while(var4.hasNext()) {
         ResolvedReferenceTypeDeclaration rrtd = (ResolvedReferenceTypeDeclaration)var4.next();
         if (rrtd.isEnum()) {
            Optional<ResolvedEnumConstantDeclaration> enumConstant = rrtd.asEnum().getEnumConstants().stream().filter((c) -> {
               return c.getName().equals(name);
            }).findFirst();
            if (enumConstant.isPresent()) {
               return SymbolReference.solved((ResolvedDeclaration)enumConstant.get());
            }
         }

         try {
            return SymbolReference.solved(rrtd.getField(((FieldAccessExpr)this.wrappedNode).getName().getId()));
         } catch (Throwable var7) {
         }
      }

      return SymbolReference.unsolved(ResolvedFieldDeclaration.class);
   }
}
