package com.github.javaparser.symbolsolver.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class Navigator {
   private Navigator() {
   }

   /** @deprecated */
   @Deprecated
   public static Node getParentNode(Node node) {
      return (Node)node.getParentNode().orElse((Object)null);
   }

   public static Node requireParentNode(Node node) {
      return (Node)node.getParentNode().orElseThrow(() -> {
         return new IllegalStateException("Parent not found, the node does not appear to be inserted in a correct AST");
      });
   }

   public static Optional<TypeDeclaration<?>> findType(CompilationUnit cu, String qualifiedName) {
      if (cu.getTypes().isEmpty()) {
         return Optional.empty();
      } else {
         String typeName = getOuterTypeName(qualifiedName);
         Optional<TypeDeclaration<?>> type = cu.getTypes().stream().filter((t) -> {
            return t.getName().getId().equals(typeName);
         }).findFirst();
         String innerTypeName = getInnerTypeName(qualifiedName);
         return type.isPresent() && !innerTypeName.isEmpty() ? findType((TypeDeclaration)type.get(), innerTypeName) : type;
      }
   }

   public static Optional<TypeDeclaration<?>> findType(TypeDeclaration<?> td, String qualifiedName) {
      String typeName = getOuterTypeName(qualifiedName);
      Optional<TypeDeclaration<?>> type = Optional.empty();
      Iterator var4 = td.getMembers().iterator();

      while(var4.hasNext()) {
         Node n = (Node)var4.next();
         if (n instanceof TypeDeclaration && ((TypeDeclaration)n).getName().getId().equals(typeName)) {
            type = Optional.of((TypeDeclaration)n);
            break;
         }
      }

      String innerTypeName = getInnerTypeName(qualifiedName);
      return type.isPresent() && !innerTypeName.isEmpty() ? findType((TypeDeclaration)type.get(), innerTypeName) : type;
   }

   public static ClassOrInterfaceDeclaration demandClass(CompilationUnit cu, String qualifiedName) {
      ClassOrInterfaceDeclaration cd = demandClassOrInterface(cu, qualifiedName);
      if (cd.isInterface()) {
         throw new IllegalStateException("Type is not a class");
      } else {
         return cd;
      }
   }

   public static ClassOrInterfaceDeclaration demandInterface(CompilationUnit cu, String qualifiedName) {
      ClassOrInterfaceDeclaration cd = demandClassOrInterface(cu, qualifiedName);
      if (!cd.isInterface()) {
         throw new IllegalStateException("Type is not an interface");
      } else {
         return cd;
      }
   }

   public static EnumDeclaration demandEnum(CompilationUnit cu, String qualifiedName) {
      Optional<TypeDeclaration<?>> res = findType(cu, qualifiedName);
      if (!res.isPresent()) {
         throw new IllegalStateException("No type found");
      } else if (!(res.get() instanceof EnumDeclaration)) {
         throw new IllegalStateException("Type is not an enum");
      } else {
         return (EnumDeclaration)res.get();
      }
   }

   public static MethodDeclaration demandMethod(TypeDeclaration<?> cd, String name) {
      MethodDeclaration found = null;
      Iterator var3 = cd.getMembers().iterator();

      while(var3.hasNext()) {
         BodyDeclaration<?> bd = (BodyDeclaration)var3.next();
         if (bd instanceof MethodDeclaration) {
            MethodDeclaration md = (MethodDeclaration)bd;
            if (md.getNameAsString().equals(name)) {
               if (found != null) {
                  throw new IllegalStateException("Ambiguous getName");
               }

               found = md;
            }
         }
      }

      if (found == null) {
         throw new IllegalStateException("No method called " + name);
      } else {
         return found;
      }
   }

   public static ConstructorDeclaration demandConstructor(TypeDeclaration<?> td, int index) {
      ConstructorDeclaration found = null;
      int i = 0;
      Iterator var4 = td.getMembers().iterator();

      while(var4.hasNext()) {
         BodyDeclaration<?> bd = (BodyDeclaration)var4.next();
         if (bd instanceof ConstructorDeclaration) {
            ConstructorDeclaration cd = (ConstructorDeclaration)bd;
            if (i == index) {
               found = cd;
               break;
            }

            ++i;
         }
      }

      if (found == null) {
         throw new IllegalStateException("No constructor with index " + index);
      } else {
         return found;
      }
   }

   public static VariableDeclarator demandField(ClassOrInterfaceDeclaration cd, String name) {
      Iterator var2 = cd.getMembers().iterator();

      while(true) {
         BodyDeclaration bd;
         do {
            if (!var2.hasNext()) {
               throw new IllegalStateException("No field with given name");
            }

            bd = (BodyDeclaration)var2.next();
         } while(!(bd instanceof FieldDeclaration));

         FieldDeclaration fd = (FieldDeclaration)bd;
         Iterator var5 = fd.getVariables().iterator();

         while(var5.hasNext()) {
            VariableDeclarator vd = (VariableDeclarator)var5.next();
            if (vd.getName().getId().equals(name)) {
               return vd;
            }
         }
      }
   }

   public static Optional<NameExpr> findNameExpression(Node node, String name) {
      return node.findFirst(NameExpr.class, (n) -> {
         return n.getNameAsString().equals(name);
      });
   }

   public static Optional<SimpleName> findSimpleName(Node node, String name) {
      return node.findFirst(SimpleName.class, (n) -> {
         return n.asString().equals(name);
      });
   }

   public static Optional<MethodCallExpr> findMethodCall(Node node, String methodName) {
      return node.findFirst(MethodCallExpr.class, (n) -> {
         return n.getNameAsString().equals(methodName);
      });
   }

   public static Optional<VariableDeclarator> demandVariableDeclaration(Node node, String name) {
      return node.findFirst(VariableDeclarator.class, (n) -> {
         return n.getNameAsString().equals(name);
      });
   }

   public static ClassOrInterfaceDeclaration demandClassOrInterface(CompilationUnit compilationUnit, String qualifiedName) {
      return (ClassOrInterfaceDeclaration)findType(compilationUnit, qualifiedName).map((res) -> {
         return (ClassOrInterfaceDeclaration)res.toClassOrInterfaceDeclaration().orElseThrow(() -> {
            return new IllegalStateException("Type is not a class or an interface, it is " + res.getClass().getCanonicalName());
         });
      }).orElseThrow(() -> {
         return new IllegalStateException("No type named '" + qualifiedName + "'found");
      });
   }

   public static SwitchStmt findSwitch(Node node) {
      return (SwitchStmt)findSwitchHelper(node).orElseThrow(IllegalArgumentException::new);
   }

   public static <N extends Node> N findNodeOfGivenClass(Node node, Class<N> clazz) {
      return (Node)node.findFirst(clazz).orElseThrow(IllegalArgumentException::new);
   }

   /** @deprecated */
   @Deprecated
   public static <N extends Node> List<N> findAllNodesOfGivenClass(Node node, Class<N> clazz) {
      return node.findAll(clazz);
   }

   public static ReturnStmt findReturnStmt(MethodDeclaration method) {
      return (ReturnStmt)findNodeOfGivenClass(method, ReturnStmt.class);
   }

   /** @deprecated */
   @Deprecated
   public static <N extends Node> Optional<N> findAncestor(Node node, Class<N> clazz) {
      return node.findAncestor(clazz);
   }

   private static String getOuterTypeName(String qualifiedName) {
      return qualifiedName.split("\\.", 2)[0];
   }

   private static String getInnerTypeName(String qualifiedName) {
      return qualifiedName.contains(".") ? qualifiedName.split("\\.", 2)[1] : "";
   }

   private static Optional<SwitchStmt> findSwitchHelper(Node node) {
      if (node instanceof SwitchStmt) {
         return Optional.of((SwitchStmt)node);
      } else {
         Iterator var1 = node.getChildNodes().iterator();

         Optional resChild;
         do {
            if (!var1.hasNext()) {
               return Optional.empty();
            }

            Node child = (Node)var1.next();
            resChild = findSwitchHelper(child);
         } while(!resChild.isPresent());

         return resChild;
      }
   }
}
