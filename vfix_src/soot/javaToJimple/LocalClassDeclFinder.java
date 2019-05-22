package soot.javaToJimple;

import polyglot.ast.LocalClassDecl;
import polyglot.ast.Node;
import polyglot.types.ClassType;
import polyglot.visit.NodeVisitor;

public class LocalClassDeclFinder extends NodeVisitor {
   private ClassType typeToFind;
   private LocalClassDecl declFound = null;

   public void typeToFind(ClassType type) {
      this.typeToFind = type;
   }

   public LocalClassDecl declFound() {
      return this.declFound;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof LocalClassDecl && ((LocalClassDecl)n).decl().type().equals(this.typeToFind)) {
         this.declFound = (LocalClassDecl)n;
      }

      return this.enter(n);
   }
}
