package com.github.javaparser.symbolsolver;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodLikeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnnotationMemberDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserConstructorDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumConstantDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserVariableDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

public class JavaSymbolSolver implements SymbolResolver {
   private TypeSolver typeSolver;

   public JavaSymbolSolver(TypeSolver typeSolver) {
      this.typeSolver = typeSolver;
   }

   public void inject(CompilationUnit destination) {
      destination.setData(Node.SYMBOL_RESOLVER_KEY, this);
   }

   public <T> T resolveDeclaration(Node node, Class<T> resultClass) {
      if (node instanceof MethodDeclaration) {
         return resultClass.cast(new JavaParserMethodDeclaration((MethodDeclaration)node, this.typeSolver));
      } else {
         ResolvedReferenceTypeDeclaration resolved;
         if (node instanceof ClassOrInterfaceDeclaration) {
            resolved = JavaParserFactory.toTypeDeclaration(node, this.typeSolver);
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof EnumDeclaration) {
            resolved = JavaParserFactory.toTypeDeclaration(node, this.typeSolver);
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof EnumConstantDeclaration) {
            ResolvedEnumDeclaration enumDeclaration = ((EnumDeclaration)node.findAncestor(EnumDeclaration.class).get()).resolve().asEnum();
            ResolvedEnumConstantDeclaration resolved = (ResolvedEnumConstantDeclaration)enumDeclaration.getEnumConstants().stream().filter((c) -> {
               return ((JavaParserEnumConstantDeclaration)c).getWrappedNode() == node;
            }).findFirst().get();
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration)node;
            TypeDeclaration<?> typeDeclaration = (TypeDeclaration)node.getParentNode().get();
            ResolvedReferenceTypeDeclaration resolvedTypeDeclaration = (ResolvedReferenceTypeDeclaration)this.resolveDeclaration(typeDeclaration, ResolvedReferenceTypeDeclaration.class);
            ResolvedConstructorDeclaration resolved = (ResolvedConstructorDeclaration)resolvedTypeDeclaration.getConstructors().stream().filter((c) -> {
               return c instanceof JavaParserConstructorDeclaration;
            }).filter((c) -> {
               return ((JavaParserConstructorDeclaration)c).getWrappedNode() == constructorDeclaration;
            }).findFirst().orElseThrow(() -> {
               return new RuntimeException("This constructor cannot be found in its parent. This seems wrong");
            });
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof AnnotationDeclaration) {
            resolved = JavaParserFactory.toTypeDeclaration(node, this.typeSolver);
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof AnnotationMemberDeclaration) {
            ResolvedAnnotationDeclaration annotationDeclaration = ((AnnotationDeclaration)node.findAncestor(AnnotationDeclaration.class).get()).resolve();
            ResolvedAnnotationMemberDeclaration resolved = (ResolvedAnnotationMemberDeclaration)annotationDeclaration.getAnnotationMembers().stream().filter((c) -> {
               return ((JavaParserAnnotationMemberDeclaration)c).getWrappedNode() == node;
            }).findFirst().get();
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof FieldDeclaration) {
            FieldDeclaration fieldDeclaration = (FieldDeclaration)node;
            if (fieldDeclaration.getVariables().size() != 1) {
               throw new RuntimeException("Cannot resolve a Field Declaration including multiple variable declarators. Resolve the single variable declarators");
            }

            ResolvedFieldDeclaration resolved = new JavaParserFieldDeclaration(fieldDeclaration.getVariable(0), this.typeSolver);
            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         if (node instanceof VariableDeclarator) {
            Object resolved;
            if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof FieldDeclaration) {
               resolved = new JavaParserFieldDeclaration((VariableDeclarator)node, this.typeSolver);
            } else {
               if (!node.getParentNode().isPresent() || !(node.getParentNode().get() instanceof VariableDeclarationExpr)) {
                  throw new UnsupportedOperationException("Parent of VariableDeclarator is: " + node.getParentNode());
               }

               resolved = new JavaParserVariableDeclaration((VariableDeclarator)node, this.typeSolver);
            }

            if (resultClass.isInstance(resolved)) {
               return resultClass.cast(resolved);
            }
         }

         SymbolReference result;
         if (node instanceof MethodCallExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((MethodCallExpr)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the method declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof ObjectCreationExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((ObjectCreationExpr)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the constructor declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof NameExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((NameExpr)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the value declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof FieldAccessExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((FieldAccessExpr)node);
            if (!result.isSolved()) {
               if (((FieldAccessExpr)node).getName().getId().equals("length")) {
                  ResolvedType scopeType = ((FieldAccessExpr)node).getScope().calculateResolvedType();
                  if (scopeType.isArray() && resultClass.isInstance(JavaSymbolSolver.ArrayLengthValueDeclaration.INSTANCE)) {
                     return resultClass.cast(JavaSymbolSolver.ArrayLengthValueDeclaration.INSTANCE);
                  }
               }

               throw new UnsolvedSymbolException("We are unable to find the value declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof ThisExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((ThisExpr)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the type declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof ExplicitConstructorInvocationStmt) {
            result = JavaParserFacade.get(this.typeSolver).solve((ExplicitConstructorInvocationStmt)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the constructor declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         if (node instanceof Parameter && ResolvedParameterDeclaration.class.equals(resultClass)) {
            Parameter parameter = (Parameter)node;
            CallableDeclaration callableDeclaration = (CallableDeclaration)node.findAncestor(CallableDeclaration.class).get();
            Object resolvedMethodLikeDeclaration;
            if (callableDeclaration.isConstructorDeclaration()) {
               resolvedMethodLikeDeclaration = callableDeclaration.asConstructorDeclaration().resolve();
            } else {
               resolvedMethodLikeDeclaration = callableDeclaration.asMethodDeclaration().resolve();
            }

            for(int i = 0; i < ((ResolvedMethodLikeDeclaration)resolvedMethodLikeDeclaration).getNumberOfParams(); ++i) {
               if (((ResolvedMethodLikeDeclaration)resolvedMethodLikeDeclaration).getParam(i).getName().equals(parameter.getNameAsString())) {
                  return resultClass.cast(((ResolvedMethodLikeDeclaration)resolvedMethodLikeDeclaration).getParam(i));
               }
            }
         }

         if (node instanceof AnnotationExpr) {
            result = JavaParserFacade.get(this.typeSolver).solve((AnnotationExpr)node);
            if (!result.isSolved()) {
               throw new UnsolvedSymbolException("We are unable to find the annotation declaration corresponding to " + node);
            }

            if (resultClass.isInstance(result.getCorrespondingDeclaration())) {
               return resultClass.cast(result.getCorrespondingDeclaration());
            }
         }

         throw new UnsupportedOperationException("Unable to find the declaration of type " + resultClass.getSimpleName() + " from " + node.getClass().getSimpleName());
      }
   }

   public <T> T toResolvedType(Type javaparserType, Class<T> resultClass) {
      ResolvedType resolvedType = JavaParserFacade.get(this.typeSolver).convertToUsage(javaparserType, (Node)javaparserType);
      if (resultClass.isInstance(resolvedType)) {
         return resultClass.cast(resolvedType);
      } else {
         throw new UnsupportedOperationException("Unable to get the resolved type of class " + resultClass.getSimpleName() + " from " + javaparserType);
      }
   }

   public ResolvedType calculateType(Expression expression) {
      return JavaParserFacade.get(this.typeSolver).getType(expression);
   }

   private static class ArrayLengthValueDeclaration implements ResolvedValueDeclaration {
      private static final JavaSymbolSolver.ArrayLengthValueDeclaration INSTANCE = new JavaSymbolSolver.ArrayLengthValueDeclaration();

      public String getName() {
         return "length";
      }

      public ResolvedType getType() {
         return ResolvedPrimitiveType.INT;
      }
   }
}
