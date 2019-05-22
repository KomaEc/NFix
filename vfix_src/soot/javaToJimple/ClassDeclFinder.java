package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class ClassDeclFinder extends NodeVisitor {
   private final ArrayList<ClassDecl> declsFound = new ArrayList();

   public ArrayList<ClassDecl> declsFound() {
      return this.declsFound;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof ClassDecl) {
         this.declsFound.add((ClassDecl)n);
      }

      return this.enter(n);
   }
}
