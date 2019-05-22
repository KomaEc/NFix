package polyglot.visit;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.FloatLit;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public class ConstantFolder extends NodeVisitor {
   TypeSystem ts;
   NodeFactory nf;

   public ConstantFolder(TypeSystem ts, NodeFactory nf) {
      this.ts = ts;
      this.nf = nf;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public NodeFactory nodeFactory() {
      return this.nf;
   }

   public Node leave(Node old, Node n, NodeVisitor v_) {
      if (!(n instanceof Expr)) {
         return n;
      } else {
         Expr e = (Expr)n;
         if (!e.isConstant()) {
            return e;
         } else {
            if (e instanceof Binary) {
               Binary b = (Binary)e;
               if (b.operator() == Binary.ADD && b.left().constantValue() instanceof String && b.right().constantValue() instanceof String) {
                  return b;
               }
            }

            Object v = e.constantValue();
            Position pos = e.position();
            if (v == null) {
               return this.nf.NullLit(pos).type(this.ts.Null());
            } else if (v instanceof String) {
               return this.nf.StringLit(pos, (String)v).type(this.ts.String());
            } else if (v instanceof Boolean) {
               return this.nf.BooleanLit(pos, (Boolean)v).type(this.ts.Boolean());
            } else if (v instanceof Double) {
               return this.nf.FloatLit(pos, FloatLit.DOUBLE, (Double)v).type(this.ts.Double());
            } else if (v instanceof Float) {
               return this.nf.FloatLit(pos, FloatLit.FLOAT, (double)(Float)v).type(this.ts.Float());
            } else if (v instanceof Long) {
               return this.nf.IntLit(pos, IntLit.LONG, (Long)v).type(this.ts.Long());
            } else if (v instanceof Integer) {
               return this.nf.IntLit(pos, IntLit.INT, (long)(Integer)v).type(this.ts.Int());
            } else {
               return v instanceof Character ? this.nf.CharLit(pos, (Character)v).type(this.ts.Char()) : e;
            }
         }
      }
   }
}
