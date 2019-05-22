package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumConstantDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Iterator;
import java.util.List;

public class EnumDeclarationContext extends AbstractJavaParserContext<EnumDeclaration> {
   private JavaParserTypeDeclarationAdapter javaParserTypeDeclarationAdapter;

   public EnumDeclarationContext(EnumDeclaration wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
      this.javaParserTypeDeclarationAdapter = new JavaParserTypeDeclarationAdapter(wrappedNode, typeSolver, this.getDeclaration(), this);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException();
      } else {
         Iterator var3 = ((EnumDeclaration)this.wrappedNode).getEntries().iterator();

         EnumConstantDeclaration constant;
         do {
            if (!var3.hasNext()) {
               if (this.getDeclaration().hasField(name)) {
                  return SymbolReference.solved(this.getDeclaration().getField(name));
               }

               return this.getParent().solveSymbol(name, typeSolver);
            }

            constant = (EnumConstantDeclaration)var3.next();
         } while(!constant.getName().getId().equals(name));

         return SymbolReference.solved(new JavaParserEnumConstantDeclaration(constant, typeSolver));
      }
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      return this.javaParserTypeDeclarationAdapter.solveType(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.javaParserTypeDeclarationAdapter.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
   }

   private ResolvedReferenceTypeDeclaration getDeclaration() {
      return new JavaParserEnumDeclaration((EnumDeclaration)this.wrappedNode, this.typeSolver);
   }
}
