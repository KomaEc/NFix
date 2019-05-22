package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.HashMap;
import java.util.Map;

public class MemoryTypeSolver implements TypeSolver {
   private TypeSolver parent;
   private Map<String, ResolvedReferenceTypeDeclaration> declarationMap = new HashMap();

   public String toString() {
      return "MemoryTypeSolver{parent=" + this.parent + ", declarationMap=" + this.declarationMap + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof MemoryTypeSolver)) {
         return false;
      } else {
         MemoryTypeSolver that = (MemoryTypeSolver)o;
         if (this.parent != null) {
            if (!this.parent.equals(that.parent)) {
               return false;
            }
         } else if (that.parent != null) {
            return false;
         }

         boolean var10000;
         label50: {
            if (this.declarationMap != null) {
               if (this.declarationMap.equals(that.declarationMap)) {
                  break label50;
               }
            } else if (that.declarationMap == null) {
               break label50;
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      int result = this.parent != null ? this.parent.hashCode() : 0;
      result = 31 * result + (this.declarationMap != null ? this.declarationMap.hashCode() : 0);
      return result;
   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   public void addDeclaration(String name, ResolvedReferenceTypeDeclaration typeDeclaration) {
      this.declarationMap.put(name, typeDeclaration);
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      return this.declarationMap.containsKey(name) ? SymbolReference.solved((ResolvedDeclaration)this.declarationMap.get(name)) : SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
   }
}
