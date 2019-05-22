package polyglot.visit;

import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Lit;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.types.Flags;
import polyglot.types.TypeSystem;

public class FlattenVisitor extends NodeVisitor {
   protected TypeSystem ts;
   protected NodeFactory nf;
   protected LinkedList stack;
   static int count = 0;
   protected Node noFlatten = null;

   public FlattenVisitor(TypeSystem ts, NodeFactory nf) {
      this.ts = ts;
      this.nf = nf;
      this.stack = new LinkedList();
   }

   public Node override(Node n) {
      return !(n instanceof FieldDecl) && !(n instanceof ConstructorCall) ? null : n;
   }

   protected static String newID() {
      return "flat$$$" + count++;
   }

   public NodeVisitor enter(Node n) {
      if (n instanceof Block) {
         this.stack.addFirst(new LinkedList());
      }

      if (n instanceof Eval) {
         Eval s = (Eval)n;
         this.noFlatten = s.expr();
      }

      return this;
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (n == this.noFlatten) {
         this.noFlatten = null;
         return n;
      } else {
         List l;
         if (n instanceof Block) {
            l = (List)this.stack.removeFirst();
            return ((Block)n).statements(l);
         } else if (n instanceof Stmt && !(n instanceof LocalDecl)) {
            l = (List)this.stack.getFirst();
            l.add(n);
            return n;
         } else if (n instanceof Expr && !(n instanceof Lit) && !(n instanceof Special) && !(n instanceof Local)) {
            Expr e = (Expr)n;
            if (e instanceof Assign) {
               return n;
            } else {
               String name = newID();
               LocalDecl def = this.nf.LocalDecl(e.position(), Flags.FINAL, this.nf.CanonicalTypeNode(e.position(), e.type()), name, e);
               def = def.localInstance(this.ts.localInstance(e.position(), Flags.FINAL, e.type(), name));
               List l = (List)this.stack.getFirst();
               l.add(def);
               Local use = this.nf.Local(e.position(), name);
               use = (Local)use.type(e.type());
               use = use.localInstance(this.ts.localInstance(e.position(), Flags.FINAL, e.type(), name));
               return use;
            }
         } else {
            return n;
         }
      }
   }
}
