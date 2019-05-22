package com.github.javaparser.symbolsolver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SourceFileInfoExtractor {
   private TypeSolver typeSolver;
   private int ok = 0;
   private int ko = 0;
   private int unsupported = 0;
   private boolean printFileName = true;
   private PrintStream out;
   private PrintStream err;
   private boolean verbose;

   public SourceFileInfoExtractor() {
      this.out = System.out;
      this.err = System.err;
      this.verbose = false;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void setPrintFileName(boolean printFileName) {
      this.printFileName = printFileName;
   }

   public void clear() {
      this.ok = 0;
      this.ko = 0;
      this.unsupported = 0;
   }

   public void setOut(PrintStream out) {
      this.out = out;
   }

   public void setErr(PrintStream err) {
      this.err = err;
   }

   public int getOk() {
      return this.ok;
   }

   public int getUnsupported() {
      return this.unsupported;
   }

   public int getKo() {
      return this.ko;
   }

   private void solveTypeDecl(ClassOrInterfaceDeclaration node) {
      ResolvedTypeDeclaration typeDeclaration = JavaParserFacade.get(this.typeSolver).getTypeDeclaration(node);
      if (typeDeclaration.isClass()) {
         this.out.println("\n[ Class " + typeDeclaration.getQualifiedName() + " ]");
         Iterator var3 = typeDeclaration.asClass().getAllSuperClasses().iterator();

         ResolvedReferenceType sc;
         while(var3.hasNext()) {
            sc = (ResolvedReferenceType)var3.next();
            this.out.println("  superclass: " + sc.getQualifiedName());
         }

         var3 = typeDeclaration.asClass().getAllInterfaces().iterator();

         while(var3.hasNext()) {
            sc = (ResolvedReferenceType)var3.next();
            this.out.println("  interface: " + sc.getQualifiedName());
         }
      }

   }

   private void solve(Node node) {
      if (node instanceof ClassOrInterfaceDeclaration) {
         this.solveTypeDecl((ClassOrInterfaceDeclaration)node);
      } else if (node instanceof Expression && !(Navigator.requireParentNode(node) instanceof ImportDeclaration) && !(Navigator.requireParentNode(node) instanceof Expression) && !(Navigator.requireParentNode(node) instanceof MethodDeclaration) && !(Navigator.requireParentNode(node) instanceof PackageDeclaration) && (Navigator.requireParentNode(node) instanceof Statement || Navigator.requireParentNode(node) instanceof VariableDeclarator)) {
         try {
            ResolvedType ref = JavaParserFacade.get(this.typeSolver).getType(node);
            this.out.println("  Line " + ((Range)node.getRange().get()).begin.line + ") " + node + " ==> " + ref.describe());
            ++this.ok;
         } catch (UnsupportedOperationException var3) {
            ++this.unsupported;
            this.err.println(var3.getMessage());
            throw var3;
         } catch (RuntimeException var4) {
            ++this.ko;
            this.err.println(var4.getMessage());
            throw var4;
         }
      }

   }

   private void solveMethodCalls(Node node) {
      if (node instanceof MethodCallExpr) {
         this.out.println("  Line " + ((Position)node.getBegin().get()).line + ") " + node + " ==> " + this.toString((MethodCallExpr)node));
      }

      Iterator var2 = node.getChildNodes().iterator();

      while(var2.hasNext()) {
         Node child = (Node)var2.next();
         this.solveMethodCalls(child);
      }

   }

   private String toString(MethodCallExpr node) {
      try {
         return this.toString(JavaParserFacade.get(this.typeSolver).solve(node));
      } catch (Exception var3) {
         if (this.verbose) {
            System.err.println("Error resolving call at L" + ((Position)node.getBegin().get()).line + ": " + node);
            var3.printStackTrace();
         }

         return "ERROR";
      }
   }

   private String toString(SymbolReference<ResolvedMethodDeclaration> methodDeclarationSymbolReference) {
      return methodDeclarationSymbolReference.isSolved() ? ((ResolvedMethodDeclaration)methodDeclarationSymbolReference.getCorrespondingDeclaration()).getQualifiedSignature() : "UNSOLVED";
   }

   private List<Node> collectAllNodes(Node node) {
      List<Node> nodes = new LinkedList();
      this.collectAllNodes(node, nodes);
      nodes.sort((n1, n2) -> {
         return ((Position)n1.getBegin().get()).compareTo((Position)n2.getBegin().get());
      });
      return nodes;
   }

   private void collectAllNodes(Node node, List<Node> nodes) {
      nodes.add(node);
      node.getChildNodes().forEach((c) -> {
         this.collectAllNodes(c, nodes);
      });
   }

   public void solve(Path path) throws IOException {
      File file = path.toFile();
      if (file.isDirectory()) {
         File[] var3 = file.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File f = var3[var5];
            this.solve(f.toPath());
         }
      } else if (file.getName().endsWith(".java")) {
         if (this.printFileName) {
            this.out.println("- parsing " + file.getAbsolutePath());
         }

         CompilationUnit cu = JavaParser.parse(file);
         List<Node> nodes = this.collectAllNodes(cu);
         nodes.forEach((n) -> {
            this.solve(n);
         });
      }

   }

   public void solveMethodCalls(Path path) throws IOException {
      File file = path.toFile();
      if (file.isDirectory()) {
         File[] var3 = file.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File f = var3[var5];
            this.solveMethodCalls(f.toPath());
         }
      } else if (file.getName().endsWith(".java")) {
         if (this.printFileName) {
            this.out.println("- parsing " + file.getAbsolutePath());
         }

         CompilationUnit cu = JavaParser.parse(file);
         this.solveMethodCalls((Node)cu);
      }

   }

   public void setTypeSolver(TypeSolver typeSolver) {
      this.typeSolver = typeSolver;
   }
}
