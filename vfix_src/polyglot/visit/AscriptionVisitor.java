package polyglot.visit;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

public class AscriptionVisitor extends ContextVisitor {
   Type type = null;
   AscriptionVisitor outer = null;

   public AscriptionVisitor(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   public AscriptionVisitor pop() {
      return this.outer;
   }

   public Type toType() {
      return this.type;
   }

   public NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
      Type t = null;
      if (parent != null && n instanceof Expr) {
         t = parent.childExpectedType((Expr)n, this);
      }

      AscriptionVisitor v = (AscriptionVisitor)this.copy();
      v.outer = this;
      v.type = t;
      return v;
   }

   public Expr ascribe(Expr e, Type toType) throws SemanticException {
      return e;
   }

   public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      if (n instanceof Expr) {
         Expr e = (Expr)n;
         Type type = ((AscriptionVisitor)v).type;
         return this.ascribe(e, type);
      } else {
         return n;
      }
   }
}
