package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class Eval_c extends Stmt_c implements Eval {
   protected Expr expr;

   public Eval_c(Position pos, Expr expr) {
      super(pos);
      this.expr = expr;
   }

   public Expr expr() {
      return this.expr;
   }

   public Eval expr(Expr expr) {
      Eval_c n = (Eval_c)this.copy();
      n.expr = expr;
      return n;
   }

   protected Eval_c reconstruct(Expr expr) {
      if (expr != this.expr) {
         Eval_c n = (Eval_c)this.copy();
         n.expr = expr;
         return n;
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Void() : child.type());
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      return this.reconstruct(expr);
   }

   public String toString() {
      return "eval(" + this.expr.toString() + ");";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      boolean semi = tr.appendSemicolon(true);
      this.print(this.expr, w, tr);
      if (semi) {
         w.write(";");
      }

      tr.appendSemicolon(semi);
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.expr, (Term)this);
      return succs;
   }
}
