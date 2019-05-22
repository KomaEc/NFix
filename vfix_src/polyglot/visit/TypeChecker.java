package polyglot.visit;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class TypeChecker extends ContextVisitor {
   public TypeChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
      return n.del().typeCheckEnter(this);
   }

   protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      Node m = n.del().typeCheck((TypeChecker)v);
      if (m instanceof Expr && ((Expr)m).type() == null) {
         throw new InternalCompilerError("Null type for " + m, m.position());
      } else {
         return m;
      }
   }
}
