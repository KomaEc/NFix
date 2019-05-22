package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.HasAccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeParameter;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.ConstructorResolutionLogic;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class JavaParserTypeDeclarationAdapter {
   private TypeDeclaration<?> wrappedNode;
   private TypeSolver typeSolver;
   private Context context;
   private ResolvedReferenceTypeDeclaration typeDeclaration;

   public JavaParserTypeDeclarationAdapter(TypeDeclaration<?> wrappedNode, TypeSolver typeSolver, ResolvedReferenceTypeDeclaration typeDeclaration, Context context) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
      this.typeDeclaration = typeDeclaration;
      this.context = context;
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      if (this.wrappedNode.getName().getId().equals(name)) {
         return SymbolReference.solved(JavaParserFacade.get(typeSolver).getTypeDeclaration(this.wrappedNode));
      } else {
         Iterator var3 = this.wrappedNode.getMembers().iterator();

         while(var3.hasNext()) {
            BodyDeclaration<?> member = (BodyDeclaration)var3.next();
            if (member instanceof TypeDeclaration) {
               TypeDeclaration<?> internalType = (TypeDeclaration)member;
               if (internalType.getName().getId().equals(name)) {
                  return SymbolReference.solved(JavaParserFacade.get(typeSolver).getTypeDeclaration(internalType));
               }

               if (name.startsWith(String.format("%s.%s", this.wrappedNode.getName(), internalType.getName()))) {
                  return JavaParserFactory.getContext(internalType, typeSolver).solveType(name.substring(this.wrappedNode.getName().getId().length() + 1), typeSolver);
               }

               if (name.startsWith(String.format("%s.", internalType.getName()))) {
                  return JavaParserFactory.getContext(internalType, typeSolver).solveType(name.substring(internalType.getName().getId().length() + 1), typeSolver);
               }
            }
         }

         if (this.wrappedNode instanceof NodeWithTypeParameters) {
            NodeWithTypeParameters<?> nodeWithTypeParameters = (NodeWithTypeParameters)this.wrappedNode;
            Iterator var9 = nodeWithTypeParameters.getTypeParameters().iterator();

            while(var9.hasNext()) {
               TypeParameter astTpRaw = (TypeParameter)var9.next();
               if (astTpRaw.getName().getId().equals(name)) {
                  return SymbolReference.solved(new JavaParserTypeParameter(astTpRaw, typeSolver));
               }
            }
         }

         ResolvedTypeDeclaration type = this.checkAncestorsForType(name, this.typeDeclaration);
         return type != null ? SymbolReference.solved(type) : this.context.getParent().solveType(name, typeSolver);
      }
   }

   private ResolvedTypeDeclaration checkAncestorsForType(String name, ResolvedReferenceTypeDeclaration declaration) {
      Iterator var3 = declaration.getAncestors(true).iterator();

      while(var3.hasNext()) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)var3.next();

         try {
            Iterator var5 = ancestor.getTypeDeclaration().internalTypes().iterator();

            while(var5.hasNext()) {
               ResolvedTypeDeclaration internalTypeDeclaration = (ResolvedTypeDeclaration)var5.next();
               boolean visible = true;
               if (internalTypeDeclaration instanceof ResolvedReferenceTypeDeclaration) {
                  ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = internalTypeDeclaration.asReferenceType();
                  if (resolvedReferenceTypeDeclaration instanceof HasAccessSpecifier) {
                     visible = ((HasAccessSpecifier)resolvedReferenceTypeDeclaration).accessSpecifier() != AccessSpecifier.PRIVATE;
                  }
               }

               if (internalTypeDeclaration.getName().equals(name)) {
                  if (visible) {
                     return internalTypeDeclaration;
                  }

                  return null;
               }
            }

            ResolvedTypeDeclaration ancestorDeclaration = this.checkAncestorsForType(name, ancestor.getTypeDeclaration());
            if (ancestorDeclaration != null) {
               return ancestorDeclaration;
            }
         } catch (UnsupportedOperationException var9) {
         }
      }

      return null;
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      List<ResolvedMethodDeclaration> candidateMethods = (List)this.typeDeclaration.getDeclaredMethods().stream().filter((m) -> {
         return m.getName().equals(name);
      }).filter((m) -> {
         return !staticOnly || m.isStatic();
      }).collect(Collectors.toList());
      if (!Object.class.getCanonicalName().equals(this.typeDeclaration.getQualifiedName())) {
         Iterator var6 = this.typeDeclaration.getAncestors(true).iterator();

         while(var6.hasNext()) {
            ResolvedReferenceType ancestor = (ResolvedReferenceType)var6.next();
            if (this.typeDeclaration != ancestor.getTypeDeclaration()) {
               candidateMethods.addAll((Collection)ancestor.getAllMethodsVisibleToInheritors().stream().filter((m) -> {
                  return m.getName().equals(name);
               }).collect(Collectors.toList()));
               SymbolReference<ResolvedMethodDeclaration> res = MethodResolutionLogic.solveMethodInType(ancestor.getTypeDeclaration(), name, argumentsTypes, staticOnly, typeSolver);
               if (res.isSolved()) {
                  candidateMethods.add(res.getCorrespondingDeclaration());
               }
            }
         }
      }

      SymbolReference res;
      if (candidateMethods.isEmpty()) {
         res = this.context.getParent().solveMethod(name, argumentsTypes, staticOnly, typeSolver);
         if (res.isSolved()) {
            candidateMethods.add(res.getCorrespondingDeclaration());
         }
      }

      if (candidateMethods.isEmpty() && this.typeDeclaration.isInterface()) {
         res = MethodResolutionLogic.solveMethodInType(new ReflectionClassDeclaration(Object.class, typeSolver), name, argumentsTypes, false, typeSolver);
         if (res.isSolved()) {
            candidateMethods.add(res.getCorrespondingDeclaration());
         }
      }

      return MethodResolutionLogic.findMostApplicable(candidateMethods, name, argumentsTypes, typeSolver);
   }

   public SymbolReference<ResolvedConstructorDeclaration> solveConstructor(List<ResolvedType> argumentsTypes, TypeSolver typeSolver) {
      return this.typeDeclaration instanceof ResolvedClassDeclaration ? ConstructorResolutionLogic.findMostApplicable(((ResolvedClassDeclaration)this.typeDeclaration).getConstructors(), argumentsTypes, typeSolver) : SymbolReference.unsolved(ResolvedConstructorDeclaration.class);
   }
}
