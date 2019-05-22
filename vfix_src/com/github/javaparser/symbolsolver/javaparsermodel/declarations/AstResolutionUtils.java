package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class AstResolutionUtils {
   static AccessSpecifier toAccessLevel(EnumSet<Modifier> modifiers) {
      return Modifier.getAccessSpecifier(modifiers);
   }

   static String containerName(Node container) {
      String packageName = getPackageName(container);
      String className = getClassName("", container);
      return packageName + (!packageName.isEmpty() && !className.isEmpty() ? "." : "") + className;
   }

   static String getPackageName(Node container) {
      if (container instanceof CompilationUnit) {
         Optional<PackageDeclaration> p = ((CompilationUnit)container).getPackageDeclaration();
         if (p.isPresent()) {
            return ((PackageDeclaration)p.get()).getName().toString();
         }
      } else if (container != null) {
         return getPackageName((Node)container.getParentNode().orElse((Object)null));
      }

      return "";
   }

   static String getClassName(String base, Node container) {
      String b;
      String cn;
      if (container instanceof ClassOrInterfaceDeclaration) {
         b = getClassName(base, (Node)container.getParentNode().orElse((Object)null));
         cn = ((ClassOrInterfaceDeclaration)container).getName().getId();
         return b.isEmpty() ? cn : b + "." + cn;
      } else if (container instanceof EnumDeclaration) {
         b = getClassName(base, (Node)container.getParentNode().orElse((Object)null));
         cn = ((EnumDeclaration)container).getName().getId();
         return b.isEmpty() ? cn : b + "." + cn;
      } else {
         return container != null ? getClassName(base, (Node)container.getParentNode().orElse((Object)null)) : base;
      }
   }

   static boolean hasDirectlyAnnotation(NodeWithAnnotations<?> nodeWithAnnotations, TypeSolver typeSolver, String canonicalName) {
      Iterator var3 = nodeWithAnnotations.getAnnotations().iterator();

      SymbolReference ref;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         AnnotationExpr annotationExpr = (AnnotationExpr)var3.next();
         ref = JavaParserFactory.getContext(annotationExpr, typeSolver).solveType(annotationExpr.getName().getId(), typeSolver);
         if (!ref.isSolved()) {
            throw new UnsolvedSymbolException(annotationExpr.getName().getId());
         }
      } while(!((ResolvedTypeDeclaration)ref.getCorrespondingDeclaration()).getQualifiedName().equals(canonicalName));

      return true;
   }

   static <N extends ResolvedReferenceTypeDeclaration> List<ResolvedConstructorDeclaration> getConstructors(NodeWithConstructors<?> wrappedNode, TypeSolver typeSolver, N container) {
      List<ResolvedConstructorDeclaration> declared = (List)wrappedNode.getConstructors().stream().map((c) -> {
         return new JavaParserConstructorDeclaration(container, c, typeSolver);
      }).collect(Collectors.toList());
      return (List)(declared.isEmpty() ? ImmutableList.of(new DefaultConstructorDeclaration(container)) : declared);
   }
}
