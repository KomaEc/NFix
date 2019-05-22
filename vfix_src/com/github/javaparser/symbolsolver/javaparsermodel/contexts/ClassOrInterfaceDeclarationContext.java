package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeParameter;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ClassOrInterfaceDeclarationContext extends AbstractJavaParserContext<ClassOrInterfaceDeclaration> {
   private JavaParserTypeDeclarationAdapter javaParserTypeDeclarationAdapter;

   public ClassOrInterfaceDeclarationContext(ClassOrInterfaceDeclaration wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
      this.javaParserTypeDeclarationAdapter = new JavaParserTypeDeclarationAdapter(wrappedNode, typeSolver, this.getDeclaration(), this);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException();
      } else {
         return this.getDeclaration().hasVisibleField(name) ? SymbolReference.solved(this.getDeclaration().getVisibleField(name)) : this.getParent().solveSymbol(name, typeSolver);
      }
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException();
      } else {
         return this.getDeclaration().hasVisibleField(name) ? Optional.of(Value.from(this.getDeclaration().getVisibleField(name))) : this.getParent().solveSymbolAsValue(name, typeSolver);
      }
   }

   public Optional<ResolvedType> solveGenericType(String name, TypeSolver typeSolver) {
      Iterator var3 = ((ClassOrInterfaceDeclaration)this.wrappedNode).getTypeParameters().iterator();

      TypeParameter tp;
      do {
         if (!var3.hasNext()) {
            return this.getParent().solveGenericType(name, typeSolver);
         }

         tp = (TypeParameter)var3.next();
      } while(!tp.getName().getId().equals(name));

      return Optional.of(new ResolvedTypeVariable(new JavaParserTypeParameter(tp, typeSolver)));
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      return this.javaParserTypeDeclarationAdapter.solveType(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.javaParserTypeDeclarationAdapter.solveMethod(name, argumentsTypes, staticOnly, typeSolver);
   }

   public SymbolReference<ResolvedConstructorDeclaration> solveConstructor(List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      return this.javaParserTypeDeclarationAdapter.solveConstructor(argumentsTypes, typeSolver);
   }

   public List<ResolvedFieldDeclaration> fieldsExposedToChild(Node child) {
      List<ResolvedFieldDeclaration> fields = new LinkedList();
      fields.addAll(((ClassOrInterfaceDeclaration)this.wrappedNode).resolve().getDeclaredFields());
      ((ClassOrInterfaceDeclaration)this.wrappedNode).getExtendedTypes().forEach((i) -> {
         fields.addAll(i.resolve().getAllFieldsVisibleToInheritors());
      });
      ((ClassOrInterfaceDeclaration)this.wrappedNode).getImplementedTypes().forEach((i) -> {
         fields.addAll(i.resolve().getAllFieldsVisibleToInheritors());
      });
      return fields;
   }

   private ResolvedReferenceTypeDeclaration getDeclaration() {
      return JavaParserFacade.get(this.typeSolver).getTypeDeclaration((ClassOrInterfaceDeclaration)this.wrappedNode);
   }
}
