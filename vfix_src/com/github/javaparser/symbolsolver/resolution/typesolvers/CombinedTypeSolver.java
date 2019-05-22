package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombinedTypeSolver implements TypeSolver {
   private TypeSolver parent;
   private List<TypeSolver> elements = new ArrayList();

   public CombinedTypeSolver(TypeSolver... elements) {
      TypeSolver[] var2 = elements;
      int var3 = elements.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TypeSolver el = var2[var4];
         this.add(el);
      }

   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   public void add(TypeSolver typeSolver) {
      this.elements.add(typeSolver);
      typeSolver.setParent(this);
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      Iterator var2 = this.elements.iterator();

      SymbolReference res;
      do {
         if (!var2.hasNext()) {
            return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
         }

         TypeSolver ts = (TypeSolver)var2.next();
         res = ts.tryToSolveType(name);
      } while(!res.isSolved());

      return res;
   }

   public ResolvedReferenceTypeDeclaration solveType(String name) throws UnsolvedSymbolException {
      SymbolReference<ResolvedReferenceTypeDeclaration> res = this.tryToSolveType(name);
      if (res.isSolved()) {
         return (ResolvedReferenceTypeDeclaration)res.getCorrespondingDeclaration();
      } else {
         throw new UnsolvedSymbolException(name);
      }
   }
}
