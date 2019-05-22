package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionFactory;
import java.util.Optional;

public class ClassLoaderTypeSolver implements TypeSolver {
   private TypeSolver parent;
   private ClassLoader classLoader;

   public ClassLoaderTypeSolver(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   protected boolean filterName(String name) {
      return true;
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      if (this.filterName(name)) {
         try {
            if (this.classLoader == null) {
               throw new RuntimeException("The ClassLoaderTypeSolver has been probably loaded through the bootstrap class loader. This usage is not supported by the JavaSymbolSolver");
            } else {
               Class<?> clazz = this.classLoader.loadClass(name);
               return SymbolReference.solved(ReflectionFactory.typeDeclarationFor(clazz, this.getRoot()));
            }
         } catch (NoClassDefFoundError var8) {
            return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
         } catch (ClassNotFoundException var9) {
            int lastDot = name.lastIndexOf(46);
            if (lastDot == -1) {
               return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
            } else {
               String parentName = name.substring(0, lastDot);
               String childName = name.substring(lastDot + 1);
               SymbolReference<ResolvedReferenceTypeDeclaration> parent = this.tryToSolveType(parentName);
               if (parent.isSolved()) {
                  Optional<ResolvedReferenceTypeDeclaration> innerClass = ((ResolvedReferenceTypeDeclaration)parent.getCorrespondingDeclaration()).internalTypes().stream().filter((it) -> {
                     return it.getName().equals(childName);
                  }).findFirst();
                  return (SymbolReference)innerClass.map(SymbolReference::solved).orElseGet(() -> {
                     return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
                  });
               } else {
                  return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
               }
            }
         }
      } else {
         return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
      }
   }
}
