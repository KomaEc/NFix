package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.FieldDecl;
import polyglot.ast.Initializer;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.visit.NodeVisitor;

public class InnerClassInfoFinder extends NodeVisitor {
   private final ArrayList<Node> localClassDeclList = new ArrayList();
   private final ArrayList<Node> anonBodyList = new ArrayList();
   private final ArrayList<Node> memberList = new ArrayList();

   public ArrayList<Node> memberList() {
      return this.memberList;
   }

   public ArrayList<Node> localClassDeclList() {
      return this.localClassDeclList;
   }

   public ArrayList<Node> anonBodyList() {
      return this.anonBodyList;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof LocalClassDecl) {
         this.localClassDeclList.add(n);
      }

      if (n instanceof New && ((New)n).anonType() != null) {
         this.anonBodyList.add(n);
      }

      if (n instanceof ProcedureDecl) {
         this.memberList.add(n);
      }

      if (n instanceof FieldDecl) {
         this.memberList.add(n);
      }

      if (n instanceof Initializer) {
         this.memberList.add(n);
      }

      return this.enter(n);
   }
}
