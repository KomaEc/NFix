package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class JavaParserTypeAdapter<T extends Node & NodeWithSimpleName<T> & NodeWithMembers<T>> {
   private T wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserTypeAdapter(T wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public String getPackageName() {
      return AstResolutionUtils.getPackageName(this.wrappedNode);
   }

   public String getClassName() {
      return AstResolutionUtils.getClassName("", this.wrappedNode);
   }

   public String getQualifiedName() {
      String containerName = AstResolutionUtils.containerName(Navigator.getParentNode(this.wrappedNode));
      return containerName.isEmpty() ? ((NodeWithSimpleName)this.wrappedNode).getName().getId() : containerName + "." + ((NodeWithSimpleName)this.wrappedNode).getName().getId();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      List<ResolvedReferenceType> ancestorsOfOther = other.getAllAncestors();
      ancestorsOfOther.add(new ReferenceTypeImpl(other, this.typeSolver));
      Iterator var3 = ancestorsOfOther.iterator();

      ResolvedReferenceType ancestorOfOther;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         ancestorOfOther = (ResolvedReferenceType)var3.next();
      } while(!ancestorOfOther.getQualifiedName().equals(this.getQualifiedName()));

      return true;
   }

   public boolean isAssignableBy(ResolvedType type) {
      if (type.isNull()) {
         return true;
      } else if (type.isReferenceType()) {
         ResolvedReferenceTypeDeclaration other = this.typeSolver.solveType(type.describe());
         return this.isAssignableBy(other);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      if (this.wrappedNode instanceof NodeWithTypeParameters) {
         NodeList<TypeParameter> typeParameters = ((NodeWithTypeParameters)this.wrappedNode).getTypeParameters();
         Iterator var4 = typeParameters.iterator();

         while(var4.hasNext()) {
            TypeParameter typeParameter = (TypeParameter)var4.next();
            if (typeParameter.getName().getId().equals(name)) {
               return SymbolReference.solved(new JavaParserTypeVariableDeclaration(typeParameter, typeSolver));
            }
         }
      }

      Iterator var7 = ((NodeWithMembers)this.wrappedNode).getMembers().iterator();

      while(var7.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var7.next();
         if (member instanceof TypeDeclaration) {
            TypeDeclaration<?> internalType = (TypeDeclaration)member;
            String prefix = internalType.getName() + ".";
            if (internalType.getName().getId().equals(name)) {
               if (internalType instanceof ClassOrInterfaceDeclaration) {
                  if (((ClassOrInterfaceDeclaration)internalType).isInterface()) {
                     return SymbolReference.solved(new JavaParserInterfaceDeclaration((ClassOrInterfaceDeclaration)internalType, typeSolver));
                  }

                  return SymbolReference.solved(new JavaParserClassDeclaration((ClassOrInterfaceDeclaration)internalType, typeSolver));
               }

               if (internalType instanceof EnumDeclaration) {
                  return SymbolReference.solved(new JavaParserEnumDeclaration((EnumDeclaration)internalType, typeSolver));
               }

               throw new UnsupportedOperationException();
            }

            if (name.startsWith(prefix) && name.length() > prefix.length()) {
               if (internalType instanceof ClassOrInterfaceDeclaration) {
                  if (((ClassOrInterfaceDeclaration)internalType).isInterface()) {
                     return (new JavaParserInterfaceDeclaration((ClassOrInterfaceDeclaration)internalType, typeSolver)).solveType(name.substring(prefix.length()), typeSolver);
                  }

                  return (new JavaParserClassDeclaration((ClassOrInterfaceDeclaration)internalType, typeSolver)).solveType(name.substring(prefix.length()), typeSolver);
               }

               if (internalType instanceof EnumDeclaration) {
                  return (new SymbolSolver(typeSolver)).solveTypeInType(new JavaParserEnumDeclaration((EnumDeclaration)internalType, typeSolver), name.substring(prefix.length()));
               }

               throw new UnsupportedOperationException();
            }
         }
      }

      return SymbolReference.unsolved(ResolvedTypeDeclaration.class);
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.wrappedNode.getParentNode().map((node) -> {
         return JavaParserFactory.toTypeDeclaration(node, this.typeSolver);
      });
   }

   public List<ResolvedFieldDeclaration> getFieldsForDeclaredVariables() {
      List<ResolvedFieldDeclaration> fields = new ArrayList();
      if (((NodeWithMembers)this.wrappedNode).getMembers() != null) {
         Iterator var2 = ((NodeWithMembers)this.wrappedNode).getMembers().iterator();

         while(true) {
            BodyDeclaration member;
            do {
               if (!var2.hasNext()) {
                  return fields;
               }

               member = (BodyDeclaration)var2.next();
            } while(!(member instanceof FieldDeclaration));

            FieldDeclaration field = (FieldDeclaration)member;
            Iterator var5 = field.getVariables().iterator();

            while(var5.hasNext()) {
               VariableDeclarator vd = (VariableDeclarator)var5.next();
               fields.add(new JavaParserFieldDeclaration(vd, this.typeSolver));
            }
         }
      } else {
         return fields;
      }
   }
}
