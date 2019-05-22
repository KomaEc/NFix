package polyglot.visit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import polyglot.ast.Block;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.util.InternalCompilerError;
import polyglot.util.UniqueID;

public class AlphaRenamer extends NodeVisitor {
   protected NodeFactory nf;
   protected Stack setStack;
   protected Map renamingMap;
   protected Set freshVars;

   public AlphaRenamer(NodeFactory nf) {
      this.nf = nf;
      this.setStack = new Stack();
      this.setStack.push(new HashSet());
      this.renamingMap = new HashMap();
      this.freshVars = new HashSet();
   }

   public NodeVisitor enter(Node n) {
      if (n instanceof Block) {
         this.setStack.push(new HashSet());
      }

      if (n instanceof LocalDecl) {
         LocalDecl l = (LocalDecl)n;
         String name = l.name();
         if (!this.freshVars.contains(name)) {
            String name_ = UniqueID.newID(name);
            this.freshVars.add(name_);
            ((Set)this.setStack.peek()).add(name);
            this.renamingMap.put(name, name_);
         }
      }

      return this;
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (n instanceof Block) {
         Set s = (Set)this.setStack.pop();
         this.renamingMap.keySet().removeAll(s);
         return n;
      } else {
         String name;
         if (n instanceof Local) {
            Local l = (Local)n;
            name = l.name();
            return (Node)(!this.renamingMap.containsKey(name) ? n : l.name((String)this.renamingMap.get(name)));
         } else if (n instanceof LocalDecl) {
            LocalDecl l = (LocalDecl)n;
            name = l.name();
            if (this.freshVars.contains(name)) {
               return n;
            } else if (!this.renamingMap.containsKey(name)) {
               throw new InternalCompilerError("Unexpected error encountered while alpha-renaming.");
            } else {
               return l.name((String)this.renamingMap.get(name));
            }
         } else {
            return n;
         }
      }
   }
}
