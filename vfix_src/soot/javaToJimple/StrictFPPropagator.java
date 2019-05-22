package soot.javaToJimple;

import polyglot.ast.ClassDecl;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class StrictFPPropagator extends NodeVisitor {
   boolean strict = false;

   public StrictFPPropagator(boolean val) {
      this.strict = val;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof ClassDecl && ((ClassDecl)n).flags().isStrictFP()) {
         return new StrictFPPropagator(true);
      } else if (n instanceof LocalClassDecl && ((LocalClassDecl)n).decl().flags().isStrictFP()) {
         return new StrictFPPropagator(true);
      } else if (n instanceof MethodDecl && ((MethodDecl)n).flags().isStrictFP()) {
         return new StrictFPPropagator(true);
      } else {
         return n instanceof ConstructorDecl && ((ConstructorDecl)n).flags().isStrictFP() ? new StrictFPPropagator(true) : this;
      }
   }

   public Node leave(Node old, Node n, NodeVisitor nodeVisitor) {
      if (n instanceof MethodDecl) {
         MethodDecl decl = (MethodDecl)n;
         if (this.strict && !decl.flags().isAbstract() && !decl.flags().isStrictFP()) {
            decl = decl.flags(decl.flags().StrictFP());
            return decl;
         }
      }

      if (n instanceof ConstructorDecl) {
         ConstructorDecl decl = (ConstructorDecl)n;
         if (this.strict && !decl.flags().isAbstract() && !decl.flags().isStrictFP()) {
            return decl.flags(decl.flags().StrictFP());
         }
      }

      if (n instanceof LocalClassDecl) {
         LocalClassDecl decl = (LocalClassDecl)n;
         if (decl.decl().flags().isStrictFP()) {
            return decl.decl().flags(decl.decl().flags().clearStrictFP());
         }
      }

      if (n instanceof ClassDecl) {
         ClassDecl decl = (ClassDecl)n;
         if (decl.flags().isStrictFP()) {
            return decl.flags(decl.flags().clearStrictFP());
         }
      }

      return n;
   }
}
