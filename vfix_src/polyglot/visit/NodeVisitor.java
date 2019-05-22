package polyglot.visit;

import polyglot.ast.Node;
import polyglot.util.InternalCompilerError;

public abstract class NodeVisitor {
   public Node override(Node parent, Node n) {
      return this.override(n);
   }

   public Node override(Node n) {
      return null;
   }

   public NodeVisitor enter(Node parent, Node n) {
      return this.enter(n);
   }

   public NodeVisitor enter(Node n) {
      return this;
   }

   public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
      return this.leave(old, n, v);
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      return n;
   }

   public NodeVisitor begin() {
      return this;
   }

   public void finish() {
   }

   public void finish(Node ast) {
      this.finish();
   }

   public String toString() {
      return this.getClass().getName();
   }

   public Node visitEdge(Node parent, Node child) {
      Node n = this.override(parent, child);
      if (n == null) {
         NodeVisitor v_ = this.enter(parent, child);
         if (v_ == null) {
            throw new InternalCompilerError("NodeVisitor.enter() returned null.");
         }

         n = child.visitChildren(v_);
         if (n == null) {
            throw new InternalCompilerError("Node_c.visitChildren() returned null.");
         }

         n = this.leave(parent, child, n, v_);
         if (n == null) {
            throw new InternalCompilerError("NodeVisitor.leave() returned null.");
         }
      }

      return n;
   }
}
