package soot.javaToJimple;

import java.util.HashSet;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.ast.Typed;
import polyglot.types.ClassType;
import polyglot.types.Type;
import polyglot.visit.NodeVisitor;

public class TypeListBuilder extends NodeVisitor {
   private final HashSet<Type> list = new HashSet();

   public HashSet<Type> getList() {
      return this.list;
   }

   public Node leave(Node old, Node n, NodeVisitor visitor) {
      if (n instanceof Typed) {
         Typed typedNode = (Typed)n;
         if (typedNode.type() instanceof ClassType) {
            this.list.add(typedNode.type());
         }
      }

      if (n instanceof ClassDecl) {
         ClassDecl cd = (ClassDecl)n;
         this.list.add(cd.type());
      }

      return n;
   }
}
