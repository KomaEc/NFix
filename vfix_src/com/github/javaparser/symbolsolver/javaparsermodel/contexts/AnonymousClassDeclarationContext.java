package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnonymousClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeParameter;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnonymousClassDeclarationContext extends AbstractJavaParserContext<ObjectCreationExpr> {
   private final JavaParserAnonymousClassDeclaration myDeclaration;

   public AnonymousClassDeclarationContext(ObjectCreationExpr node, TypeSolver typeSolver) {
      super(node, typeSolver);
      this.myDeclaration = new JavaParserAnonymousClassDeclaration((ObjectCreationExpr)this.wrappedNode, this.typeSolver);
      Preconditions.checkArgument(node.getAnonymousClassBody().isPresent(), "An anonymous class must have a body");
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      List<ResolvedMethodDeclaration> candidateMethods = (List)this.myDeclaration.getDeclaredMethods().stream().filter((m) -> {
         return m.getName().equals(name) && (!staticOnly || m.isStatic());
      }).collect(Collectors.toList());
      if (!Object.class.getCanonicalName().equals(this.myDeclaration.getQualifiedName())) {
         Iterator var6 = this.myDeclaration.getAncestors().iterator();

         while(var6.hasNext()) {
            ResolvedReferenceType ancestor = (ResolvedReferenceType)var6.next();
            SymbolReference<ResolvedMethodDeclaration> res = MethodResolutionLogic.solveMethodInType(ancestor.getTypeDeclaration(), name, argumentsTypes, staticOnly, typeSolver);
            if (res.isSolved()) {
               candidateMethods.add(res.getCorrespondingDeclaration());
            }
         }
      }

      SymbolReference res;
      if (candidateMethods.isEmpty()) {
         res = this.getParent().solveMethod(name, argumentsTypes, staticOnly, typeSolver);
         if (res.isSolved()) {
            candidateMethods.add(res.getCorrespondingDeclaration());
         }
      }

      if (candidateMethods.isEmpty() && this.myDeclaration.getSuperTypeDeclaration().isInterface()) {
         res = MethodResolutionLogic.solveMethodInType(new ReflectionClassDeclaration(Object.class, typeSolver), name, argumentsTypes, false, typeSolver);
         if (res.isSolved()) {
            candidateMethods.add(res.getCorrespondingDeclaration());
         }
      }

      return MethodResolutionLogic.findMostApplicable(candidateMethods, name, argumentsTypes, typeSolver);
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      List<TypeDeclaration> typeDeclarations = this.myDeclaration.findMembersOfKind(TypeDeclaration.class);
      Optional<SymbolReference<ResolvedTypeDeclaration>> exactMatch = typeDeclarations.stream().filter((internalType) -> {
         return internalType.getName().getId().equals(name);
      }).findFirst().map((internalType) -> {
         return SymbolReference.solved(JavaParserFacade.get(typeSolver).getTypeDeclaration(internalType));
      });
      if (exactMatch.isPresent()) {
         return (SymbolReference)exactMatch.get();
      } else {
         Optional<SymbolReference<ResolvedTypeDeclaration>> recursiveMatch = typeDeclarations.stream().filter((internalType) -> {
            return name.startsWith(String.format("%s.", internalType.getName()));
         }).findFirst().map((internalType) -> {
            return JavaParserFactory.getContext(internalType, typeSolver).solveType(name.substring(internalType.getName().getId().length() + 1), typeSolver);
         });
         if (recursiveMatch.isPresent()) {
            return (SymbolReference)recursiveMatch.get();
         } else {
            Optional<SymbolReference<ResolvedTypeDeclaration>> typeArgumentsMatch = ((NodeList)((ObjectCreationExpr)this.wrappedNode).getTypeArguments().map((nodes) -> {
               return (NodeList)((NodeWithTypeArguments)nodes).getTypeArguments().orElse(new NodeList());
            }).orElse(new NodeList())).stream().filter((type) -> {
               return type.toString().equals(name);
            }).findFirst().map((matchingType) -> {
               return SymbolReference.solved(new JavaParserTypeParameter(new TypeParameter(matchingType.toString()), typeSolver));
            });
            if (typeArgumentsMatch.isPresent()) {
               return (SymbolReference)typeArgumentsMatch.get();
            } else {
               Iterator var7 = this.myDeclaration.getAncestors().iterator();

               while(var7.hasNext()) {
                  ResolvedReferenceType ancestor = (ResolvedReferenceType)var7.next();
                  if (ancestor.getTypeDeclaration().getName().equals(name)) {
                     return SymbolReference.solved(ancestor.getTypeDeclaration());
                  }

                  try {
                     Iterator var9 = ancestor.getTypeDeclaration().internalTypes().iterator();

                     while(var9.hasNext()) {
                        ResolvedTypeDeclaration internalTypeDeclaration = (ResolvedTypeDeclaration)var9.next();
                        if (internalTypeDeclaration.getName().equals(name)) {
                           return SymbolReference.solved(internalTypeDeclaration);
                        }
                     }
                  } catch (UnsupportedOperationException var11) {
                  }
               }

               return this.getParent().solveType(name, typeSolver);
            }
         }
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Preconditions.checkArgument(typeSolver != null);
      return this.myDeclaration.hasVisibleField(name) ? SymbolReference.solved(this.myDeclaration.getVisibleField(name)) : this.getParent().solveSymbol(name, typeSolver);
   }
}
