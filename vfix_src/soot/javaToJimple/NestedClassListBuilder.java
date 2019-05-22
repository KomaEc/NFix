package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.ClassDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.types.ClassType;
import polyglot.visit.NodeVisitor;

public class NestedClassListBuilder extends NodeVisitor {
   private final ArrayList<Node> classDeclsList = new ArrayList();
   private final ArrayList<Node> anonClassBodyList = new ArrayList();
   private final ArrayList<Node> nestedUsedList = new ArrayList();

   public ArrayList<Node> getClassDeclsList() {
      return this.classDeclsList;
   }

   public ArrayList<Node> getAnonClassBodyList() {
      return this.anonClassBodyList;
   }

   public ArrayList<Node> getNestedUsedList() {
      return this.nestedUsedList;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof New) {
         if (((New)n).anonType() != null && ((New)n).body() != null) {
            this.anonClassBodyList.add(n);
         } else if (((ClassType)((New)n).objectType().type()).isNested()) {
            this.nestedUsedList.add(n);
         }
      }

      if (n instanceof ClassDecl && ((ClassDecl)n).type().isNested()) {
         this.classDeclsList.add(n);
      }

      return this.enter(n);
   }
}
