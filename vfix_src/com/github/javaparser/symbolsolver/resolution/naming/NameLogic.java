package com.github.javaparser.symbolsolver.resolution.naming;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

public class NameLogic {
   public static boolean isSimpleName(Node node) {
      return !isQualifiedName(node);
   }

   public static boolean isQualifiedName(Node node) {
      if (!isAName(node)) {
         throw new IllegalArgumentException();
      } else {
         return nameAsString(node).contains(".");
      }
   }

   public static boolean isAName(Node node) {
      if (node instanceof FieldAccessExpr) {
         FieldAccessExpr fieldAccessExpr = (FieldAccessExpr)node;
         return isAName(fieldAccessExpr.getScope());
      } else {
         return node instanceof SimpleName || node instanceof Name || node instanceof ClassOrInterfaceType || node instanceof NameExpr;
      }
   }

   private static Node getQualifier(Node node) {
      if (node instanceof FieldAccessExpr) {
         FieldAccessExpr fieldAccessExpr = (FieldAccessExpr)node;
         return fieldAccessExpr.getScope();
      } else {
         throw new UnsupportedOperationException(node.getClass().getCanonicalName());
      }
   }

   private static Node getRightMostName(Node node) {
      if (node instanceof FieldAccessExpr) {
         FieldAccessExpr fieldAccessExpr = (FieldAccessExpr)node;
         return fieldAccessExpr.getName();
      } else {
         throw new UnsupportedOperationException(node.getClass().getCanonicalName());
      }
   }

   public static NameRole classifyRole(Node name) {
      if (!isAName(name)) {
         throw new IllegalArgumentException("The given node is not a name");
      } else if (!name.getParentNode().isPresent()) {
         throw new IllegalArgumentException("We cannot understand the role of a name if it has no parent");
      } else if (whenParentIs(Name.class, name, (p, c) -> {
         return p.getQualifier().isPresent() && p.getQualifier().get() == c;
      })) {
         return classifyRole((Node)name.getParentNode().get());
      } else if (whenParentIs(PackageDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(ImportDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MarkerAnnotationExpr.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ClassOrInterfaceDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(ClassOrInterfaceDeclaration.class, name, (p, c) -> {
         return p.getExtendedTypes().contains((Object)c) || p.getImplementedTypes().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ClassOrInterfaceType.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(NameExpr.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(FieldAccessExpr.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MethodDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(Parameter.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(MethodCallExpr.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ConstructorDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(AnnotationDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(AnnotationMemberDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(AnnotationMemberDeclaration.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MethodDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(MethodDeclaration.class, name, (p, c) -> {
         return p.getType() == c || p.getThrownExceptions().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(Parameter.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(Parameter.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ReceiverParameter.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MethodCallExpr.class, name, (p, c) -> {
         return p.getName() == c || p.getTypeArguments().isPresent() && ((NodeList)p.getTypeArguments().get()).contains((Object)c) || p.getScope().isPresent() && p.getScope().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ConstructorDeclaration.class, name, (p, c) -> {
         return p.getName() == c || p.getThrownExceptions().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(TypeParameter.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(EnumDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(EnumConstantDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(FieldAccessExpr.class, name, (p, c) -> {
         return p.getName() == c || p.getScope() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ObjectCreationExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ReturnStmt.class, name, (p, c) -> {
         return p.getExpression().isPresent() && p.getExpression().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleDeclaration.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(ModuleRequiresStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleExportsStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleExportsStmt.class, name, (p, c) -> {
         return p.getModuleNames().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleOpensStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleOpensStmt.class, name, (p, c) -> {
         return p.getModuleNames().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleUsesStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ModuleProvidesStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ClassExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ThisExpr.class, name, (p, c) -> {
         return p.getClassExpr().isPresent() && p.getClassExpr().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(SuperExpr.class, name, (p, c) -> {
         return p.getClassExpr().isPresent() && p.getClassExpr().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ArrayCreationExpr.class, name, (p, c) -> {
         return p.getElementType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(CastExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(InstanceOfExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(TypeExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ArrayAccessExpr.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(UnaryExpr.class, name, (p, c) -> {
         return p.getExpression() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(AssignExpr.class, name, (p, c) -> {
         return p.getTarget() == c || p.getValue() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(TryStmt.class, name, (p, c) -> {
         return p.getResources().contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(VariableDeclarator.class, name, (p, c) -> {
         return p.getInitializer().isPresent() && p.getInitializer().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MemberValuePair.class, name, (p, c) -> {
         return p.getValue() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(MemberValuePair.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return NameRole.DECLARATION;
      } else if (whenParentIs(ExplicitConstructorInvocationStmt.class, name, (p, c) -> {
         return p.getExpression().isPresent() && p.getExpression().get() == c || p.getTypeArguments().isPresent() && ((NodeList)p.getTypeArguments().get()).contains((Object)c);
      })) {
         return NameRole.REFERENCE;
      } else if (whenParentIs(ObjectCreationExpr.class, name, (p, c) -> {
         return p.getType() == c || p.getScope().isPresent() && p.getScope().get() == c;
      })) {
         return NameRole.REFERENCE;
      } else if (name.getParentNode().isPresent() && isAName((Node)name.getParentNode().get())) {
         return classifyRole((Node)name.getParentNode().get());
      } else {
         throw new UnsupportedOperationException("Unable to classify role of name contained in " + ((Node)name.getParentNode().get()).getClass().getSimpleName());
      }
   }

   public static NameCategory classifyReference(Node name, TypeSolver typeSolver) {
      if (!name.getParentNode().isPresent()) {
         throw new IllegalArgumentException("We cannot understand the category of a name if it has no parent");
      } else if (classifyRole(name) != NameRole.REFERENCE) {
         throw new IllegalArgumentException("This method can be used only to classify names used as references");
      } else {
         NameCategory first = syntacticClassificationAccordingToContext(name);
         if (first.isNeedingDisambiguation()) {
            NameCategory second = reclassificationOfContextuallyAmbiguousNames(name, first, typeSolver);

            assert !second.isNeedingDisambiguation();

            return second;
         } else {
            return first;
         }
      }
   }

   private static NameCategory reclassificationOfContextuallyAmbiguousNames(Node name, NameCategory ambiguousCategory, TypeSolver typeSolver) {
      if (!ambiguousCategory.isNeedingDisambiguation()) {
         throw new IllegalArgumentException("The Name Category is not ambiguous: " + ambiguousCategory);
      } else if (ambiguousCategory == NameCategory.AMBIGUOUS_NAME && isSimpleName(name)) {
         return reclassificationOfContextuallyAmbiguousSimpleAmbiguousName(name, typeSolver);
      } else if (ambiguousCategory == NameCategory.AMBIGUOUS_NAME && isQualifiedName(name)) {
         return reclassificationOfContextuallyAmbiguousQualifiedAmbiguousName(name, typeSolver);
      } else if (ambiguousCategory == NameCategory.PACKAGE_OR_TYPE_NAME) {
         return reclassificationOfContextuallyAmbiguosPackageOrTypeName(name, typeSolver);
      } else {
         throw new UnsupportedOperationException("I do not know how to handle this semantic reclassification of ambiguous name categories");
      }
   }

   private static NameCategory reclassificationOfContextuallyAmbiguosPackageOrTypeName(Node name, TypeSolver typeSolver) {
      if (isSimpleName(name)) {
         return JavaParserFactory.getContext(name, typeSolver).solveType(nameAsString(name), typeSolver).isSolved() ? NameCategory.TYPE_NAME : NameCategory.PACKAGE_NAME;
      } else if (isQualifiedName(name)) {
         return JavaParserFactory.getContext(name, typeSolver).solveType(nameAsString(name), typeSolver).isSolved() ? NameCategory.TYPE_NAME : NameCategory.PACKAGE_NAME;
      } else {
         throw new UnsupportedOperationException("This is unexpected: the name is neither simple or qualified");
      }
   }

   private static NameCategory reclassificationOfContextuallyAmbiguousQualifiedAmbiguousName(Node nameNode, TypeSolver typeSolver) {
      Node leftName = getQualifier(nameNode);
      String rightName = nameAsString(getRightMostName(nameNode));
      NameCategory leftNameCategory = classifyReference(leftName, typeSolver);
      if (leftNameCategory == NameCategory.PACKAGE_NAME) {
         return typeSolver.hasType(nameAsString(nameNode)) ? NameCategory.TYPE_NAME : NameCategory.PACKAGE_NAME;
      } else if (leftNameCategory == NameCategory.TYPE_NAME) {
         SymbolReference<ResolvedTypeDeclaration> scopeTypeRef = JavaParserFactory.getContext(leftName, typeSolver).solveType(nameAsString(leftName), typeSolver);
         if (scopeTypeRef.isSolved()) {
            ResolvedTypeDeclaration scopeType = (ResolvedTypeDeclaration)scopeTypeRef.getCorrespondingDeclaration();
            if (scopeType instanceof ResolvedReferenceTypeDeclaration) {
               ResolvedReferenceTypeDeclaration scopeRefType = scopeType.asReferenceType();
               if (scopeRefType.getAllMethods().stream().anyMatch((m) -> {
                  return m.getName().equals(rightName);
               })) {
                  return NameCategory.EXPRESSION_NAME;
               } else if (scopeRefType.getAllFields().stream().anyMatch((f) -> {
                  return f.isStatic() && f.getName().equals(rightName);
               })) {
                  return NameCategory.EXPRESSION_NAME;
               } else {
                  return scopeRefType.hasInternalType(rightName) ? NameCategory.TYPE_NAME : NameCategory.COMPILATION_ERROR;
               }
            } else {
               throw new UnsupportedOperationException("The name is a type but it has been resolved to something that is not a reference type");
            }
         } else {
            throw new UnsolvedSymbolException("Unable to solve context type: " + nameAsString(leftName));
         }
      } else if (leftNameCategory == NameCategory.EXPRESSION_NAME) {
         return NameCategory.EXPRESSION_NAME;
      } else {
         throw new UnsupportedOperationException("I do not know how to handle this semantic reclassification of ambiguous name categories");
      }
   }

   private static NameCategory reclassificationOfContextuallyAmbiguousSimpleAmbiguousName(Node nameNode, TypeSolver typeSolver) {
      String name = nameAsString(nameNode);
      Context context = JavaParserFactory.getContext(nameNode, typeSolver);
      if (context.localVariableDeclarationInScope(name).isPresent()) {
         return NameCategory.EXPRESSION_NAME;
      } else if (context.parameterDeclarationInScope(name).isPresent()) {
         return NameCategory.EXPRESSION_NAME;
      } else {
         return context.fieldDeclarationInScope(name).isPresent() ? NameCategory.EXPRESSION_NAME : NameCategory.PACKAGE_NAME;
      }
   }

   public static NameCategory syntacticClassificationAccordingToContext(Node name) {
      if (name.getParentNode().isPresent()) {
         Node parent = (Node)name.getParentNode().get();
         if (isAName(parent) && nameAsString(name).equals(nameAsString(parent))) {
            return syntacticClassificationAccordingToContext(parent);
         }
      }

      if (isSyntacticallyATypeName(name)) {
         return NameCategory.TYPE_NAME;
      } else if (isSyntacticallyAnExpressionName(name)) {
         return NameCategory.EXPRESSION_NAME;
      } else if (isSyntacticallyAMethodName(name)) {
         return NameCategory.METHOD_NAME;
      } else if (isSyntacticallyAPackageOrTypeName(name)) {
         return NameCategory.PACKAGE_OR_TYPE_NAME;
      } else if (isSyntacticallyAAmbiguousName(name)) {
         return NameCategory.AMBIGUOUS_NAME;
      } else if (isSyntacticallyAModuleName(name)) {
         return NameCategory.MODULE_NAME;
      } else if (isSyntacticallyAPackageName(name)) {
         return NameCategory.PACKAGE_NAME;
      } else if (name instanceof NameExpr) {
         return NameCategory.EXPRESSION_NAME;
      } else if (name instanceof FieldAccessExpr) {
         return NameCategory.EXPRESSION_NAME;
      } else if (name instanceof ClassOrInterfaceType) {
         return NameCategory.TYPE_NAME;
      } else if (name.getParentNode().isPresent() && name.getParentNode().get() instanceof ClassOrInterfaceType) {
         return NameCategory.TYPE_NAME;
      } else if (name.getParentNode().isPresent() && name.getParentNode().get() instanceof FieldAccessExpr) {
         return NameCategory.EXPRESSION_NAME;
      } else {
         throw new UnsupportedOperationException("Unable to classify category of name contained in " + ((Node)name.getParentNode().get()).getClass().getSimpleName() + ". See " + name + " at " + name.getRange());
      }
   }

   private static boolean isSyntacticallyAAmbiguousName(Node name) {
      if (whenParentIs(FieldAccessExpr.class, name, (p, c) -> {
         return p.getScope() == c;
      })) {
         return true;
      } else if (whenParentIs(MethodCallExpr.class, name, (p, c) -> {
         return p.getScope().isPresent() && p.getScope().get() == c;
      })) {
         return true;
      } else {
         return whenParentIs(MemberValuePair.class, name, (p, c) -> {
            return p.getValue() == c;
         });
      }
   }

   private static boolean isSyntacticallyAPackageOrTypeName(Node name) {
      if (whenParentIs(ClassOrInterfaceType.class, name, (p, c) -> {
         return p.getScope().isPresent() && p.getScope().get() == c && (isSyntacticallyATypeName(p) || isSyntacticallyAPackageOrTypeName(p));
      })) {
         return true;
      } else {
         return whenParentIs(ImportDeclaration.class, name, (p, c) -> {
            return !p.isStatic() && p.isAsterisk() && p.getName() == name;
         });
      }
   }

   private static boolean isSyntacticallyAMethodName(Node name) {
      return whenParentIs(MethodCallExpr.class, name, (p, c) -> {
         return p.getName() == c;
      });
   }

   private static boolean isSyntacticallyAModuleName(Node name) {
      if (whenParentIs(ModuleRequiresStmt.class, name, (p, c) -> {
         return p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(ModuleExportsStmt.class, name, (p, c) -> {
         return p.getModuleNames().contains((Object)name);
      })) {
         return true;
      } else {
         return whenParentIs(ModuleOpensStmt.class, name, (p, c) -> {
            return p.getModuleNames().contains((Object)name);
         });
      }
   }

   private static boolean isSyntacticallyAPackageName(Node name) {
      if (whenParentIs(ModuleExportsStmt.class, name, (p, c) -> {
         return p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(ModuleOpensStmt.class, name, (p, c) -> {
         return p.getName() == name;
      })) {
         return true;
      } else {
         return whenParentIs(Name.class, name, (p, c) -> {
            return p.getQualifier().isPresent() && p.getQualifier().get() == name && isSyntacticallyAPackageName(p);
         });
      }
   }

   private static boolean isSyntacticallyATypeName(Node name) {
      if (whenParentIs(ModuleUsesStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return true;
      } else if (whenParentIs(ModuleProvidesStmt.class, name, (p, c) -> {
         return p.getName() == c;
      })) {
         return true;
      } else if (whenParentIs(ImportDeclaration.class, name, (p, c) -> {
         return !p.isStatic() && !p.isAsterisk() && p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(Name.class, name, (largerName, c) -> {
         return whenParentIs(ImportDeclaration.class, largerName, (importDecl, c2) -> {
            return importDecl.isStatic() && !importDecl.isAsterisk() && importDecl.getName() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(ImportDeclaration.class, name, (importDecl, c2) -> {
         return importDecl.isStatic() && !importDecl.isAsterisk() && importDecl.getName() == c2;
      })) {
         return true;
      } else if (whenParentIs(ImportDeclaration.class, name, (p, c) -> {
         return p.isStatic() && p.isAsterisk() && p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(ConstructorDeclaration.class, name, (p, c) -> {
         return p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(AnnotationExpr.class, name, (p, c) -> {
         return p.getName() == name;
      })) {
         return true;
      } else if (whenParentIs(ClassExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(ThisExpr.class, nameExpr, (ne, c2) -> {
            return ne.getClassExpr().isPresent() && ne.getClassExpr().get() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(ThisExpr.class, name, (ne, c2) -> {
         return ne.getClassExpr().isPresent() && ne.getClassExpr().get() == c2;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(SuperExpr.class, nameExpr, (ne, c2) -> {
            return ne.getClassExpr().isPresent() && ne.getClassExpr().get() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(SuperExpr.class, name, (ne, c2) -> {
         return ne.getClassExpr().isPresent() && ne.getClassExpr().get() == c2;
      })) {
         return true;
      } else if (whenParentIs(ClassOrInterfaceDeclaration.class, name, (p, c) -> {
         return p.getExtendedTypes().contains((Object)c) || p.getImplementedTypes().contains((Object)c);
      })) {
         return true;
      } else if (whenParentIs(MethodDeclaration.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(AnnotationMemberDeclaration.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(MethodDeclaration.class, name, (p, c) -> {
         return p.getThrownExceptions().contains((Object)c);
      })) {
         return true;
      } else if (whenParentIs(ConstructorDeclaration.class, name, (p, c) -> {
         return p.getThrownExceptions().contains((Object)c);
      })) {
         return true;
      } else if (whenParentIs(VariableDeclarator.class, name, (p1, c1) -> {
         return p1.getType() == c1 && whenParentIs(FieldDeclaration.class, p1, (p2, c2) -> {
            return p2.getVariables().contains((Node)c2);
         });
      })) {
         return true;
      } else if (whenParentIs(Parameter.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(ReceiverParameter.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(VariableDeclarator.class, name, (p1, c1) -> {
         return p1.getType() == c1 && whenParentIs(VariableDeclarationExpr.class, p1, (p2, c2) -> {
            return p2.getVariables().contains((Node)c2);
         });
      })) {
         return true;
      } else if (whenParentIs(ClassOrInterfaceType.class, name, (p, c) -> {
         return p.getTypeArguments().isPresent() && ((NodeList)p.getTypeArguments().get()).contains((Object)c);
      })) {
         return true;
      } else if (whenParentIs(MethodCallExpr.class, name, (p, c) -> {
         return p.getTypeArguments().isPresent() && ((NodeList)p.getTypeArguments().get()).contains((Object)c);
      })) {
         return true;
      } else if (whenParentIs(ObjectCreationExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(ArrayCreationExpr.class, name, (p, c) -> {
         return p.getElementType() == c;
      })) {
         return true;
      } else if (whenParentIs(CastExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else if (whenParentIs(InstanceOfExpr.class, name, (p, c) -> {
         return p.getType() == c;
      })) {
         return true;
      } else {
         return whenParentIs(TypeExpr.class, name, (p1, c1) -> {
            return p1.getType() == c1 && whenParentIs(MethodReferenceExpr.class, p1, (p2, c2) -> {
               return p2.getScope() == c2;
            });
         });
      }
   }

   private static boolean isSyntacticallyAnExpressionName(Node name) {
      if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(ExplicitConstructorInvocationStmt.class, nameExpr, (ne, c2) -> {
            return ne.getExpression().isPresent() && ne.getExpression().get() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(ExplicitConstructorInvocationStmt.class, name, (ne, c2) -> {
         return ne.getExpression().isPresent() && ne.getExpression().get() == c2;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(ObjectCreationExpr.class, nameExpr, (ne, c2) -> {
            return ne.getScope().isPresent() && ne.getScope().get() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(ObjectCreationExpr.class, name, (ne, c2) -> {
         return ne.getScope().isPresent() && ne.getScope().get() == c2;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(ArrayAccessExpr.class, nameExpr, (ne, c2) -> {
            return ne.getName() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(ArrayAccessExpr.class, name, (ne, c2) -> {
         return ne.getName() == c2;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(UnaryExpr.class, nameExpr, (ne, c2) -> {
            return ne.getExpression() == c2 && ne.isPostfix();
         });
      })) {
         return true;
      } else if (whenParentIs(UnaryExpr.class, name, (ne, c2) -> {
         return ne.getExpression() == c2 && ne.isPostfix();
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(AssignExpr.class, nameExpr, (ne, c2) -> {
            return ne.getTarget() == c2;
         });
      })) {
         return true;
      } else if (whenParentIs(AssignExpr.class, name, (ne, c2) -> {
         return ne.getTarget() == c2;
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (nameExpr, c) -> {
         return nameExpr.getName() == c && whenParentIs(TryStmt.class, nameExpr, (ne, c2) -> {
            return ne.getResources().contains((Node)c2);
         });
      })) {
         return true;
      } else if (whenParentIs(NameExpr.class, name, (p1, c1) -> {
         return p1.getName() == c1 && whenParentIs(VariableDeclarator.class, p1, (p2, c2) -> {
            return p2.getInitializer().isPresent() && p2.getInitializer().get() == c2 && whenParentIs(VariableDeclarationExpr.class, p2, (p3, c3) -> {
               return p3.getVariables().contains((Node)c3) && whenParentIs(TryStmt.class, p3, (p4, c4) -> {
                  return p4.getResources().contains((Node)c4);
               });
            });
         });
      })) {
         return true;
      } else if (whenParentIs(TryStmt.class, name, (ne, c2) -> {
         return ne.getResources().contains((Object)c2);
      })) {
         return true;
      } else {
         return whenParentIs(VariableDeclarator.class, name, (p2, c2) -> {
            return p2.getInitializer().isPresent() && p2.getInitializer().get() == c2 && whenParentIs(VariableDeclarationExpr.class, p2, (p3, c3) -> {
               return p3.getVariables().contains((Node)c3) && whenParentIs(TryStmt.class, p3, (p4, c4) -> {
                  return p4.getResources().contains((Node)c4);
               });
            });
         });
      }
   }

   public static String nameAsString(Node name) {
      if (!isAName(name)) {
         throw new IllegalArgumentException("A name was expected");
      } else if (name instanceof Name) {
         return ((Name)name).asString();
      } else if (name instanceof SimpleName) {
         return ((SimpleName)name).getIdentifier();
      } else if (name instanceof ClassOrInterfaceType) {
         return ((ClassOrInterfaceType)name).asString();
      } else if (name instanceof FieldAccessExpr) {
         FieldAccessExpr fieldAccessExpr = (FieldAccessExpr)name;
         if (isAName(fieldAccessExpr.getScope())) {
            return nameAsString(fieldAccessExpr.getScope()) + "." + nameAsString(fieldAccessExpr.getName());
         } else {
            throw new IllegalArgumentException();
         }
      } else if (name instanceof NameExpr) {
         return ((NameExpr)name).getNameAsString();
      } else {
         throw new UnsupportedOperationException("Unknown type of name found: " + name + " (" + name.getClass().getCanonicalName() + ")");
      }
   }

   private static <P extends Node, C extends Node> boolean whenParentIs(Class<P> parentClass, C child) {
      return whenParentIs(parentClass, child, (p, c) -> {
         return true;
      });
   }

   private static <P extends Node, C extends Node> boolean whenParentIs(Class<P> parentClass, C child, NameLogic.PredicateOnParentAndChild<P, C> predicate) {
      if (!child.getParentNode().isPresent()) {
         return false;
      } else {
         Node parent = (Node)child.getParentNode().get();
         return parentClass.isInstance(parent) && predicate.isSatisfied((Node)parentClass.cast(parent), child);
      }
   }

   private interface PredicateOnParentAndChild<P extends Node, C extends Node> {
      boolean isSatisfied(P var1, C var2);
   }
}
