package soot.javaToJimple;

import polyglot.ast.Assert;
import polyglot.ast.ClassDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class AssertStmtChecker extends NodeVisitor {
   private boolean hasAssert = false;

   public boolean isHasAssert() {
      return this.hasAssert;
   }

   public Node override(Node parent, Node n) {
      if (n instanceof ClassDecl) {
         return n;
      } else {
         return n instanceof New && ((New)n).anonType() != null ? n : null;
      }
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof Assert) {
         this.hasAssert = true;
      }

      return this.enter(n);
   }
}
