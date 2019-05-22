package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class ClassLiteralChecker extends NodeVisitor {
   private final ArrayList<Node> list = new ArrayList();

   public ArrayList<Node> getList() {
      return this.list;
   }

   public Node override(Node parent, Node n) {
      if (n instanceof ClassDecl) {
         return n;
      } else {
         return n instanceof New && ((New)n).anonType() != null ? n : null;
      }
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof ClassLit) {
         ClassLit lit = (ClassLit)n;
         if (!lit.typeNode().type().isPrimitive()) {
            this.list.add(n);
         }
      }

      return this.enter(n);
   }
}
