package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.Throw;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Throw_c extends Stmt_c implements Throw {
   protected Expr expr;

   public Throw_c(Position pos, Expr expr) {
      super(pos);
      this.expr = expr;
   }

   public Expr expr() {
      return this.expr;
   }

   public Throw expr(Expr expr) {
      Throw_c n = (Throw_c)this.copy();
      n.expr = expr;
      return n;
   }

   protected Throw_c reconstruct(Expr expr) {
      if (expr != this.expr) {
         Throw_c n = (Throw_c)this.copy();
         n.expr = expr;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      return this.reconstruct(expr);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      if (!this.expr.type().isThrowable()) {
         throw new SemanticException("Can only throw subclasses of \"" + tc.typeSystem().Throwable() + "\".", this.expr.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Throwable() : child.type());
   }

   public String toString() {
      return "throw " + this.expr + ";";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("throw ");
      this.print(this.expr, w, tr);
      w.write(";");
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.expr, (Term)this);
      return Collections.EMPTY_LIST;
   }

   public List throwTypes(TypeSystem ts) {
      return CollectionUtil.list(this.expr.type(), ts.NullPointerException());
   }
}
