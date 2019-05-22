package soot.javaToJimple;

import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.visit.NodeVisitor;

public class ReturnStmtChecker extends NodeVisitor {
   private boolean hasReturn = false;

   public boolean hasRet() {
      return this.hasReturn;
   }

   public Node leave(Node old, Node n, NodeVisitor visitor) {
      if (n instanceof Return) {
         this.hasReturn = true;
      }

      return n;
   }
}
