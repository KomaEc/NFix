package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Synchronized;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Synchronized_c extends Stmt_c implements Synchronized {
   protected Expr expr;
   protected Block body;

   public Synchronized_c(Position pos, Expr expr, Block body) {
      super(pos);
      this.expr = expr;
      this.body = body;
   }

   public Expr expr() {
      return this.expr;
   }

   public Synchronized expr(Expr expr) {
      Synchronized_c n = (Synchronized_c)this.copy();
      n.expr = expr;
      return n;
   }

   public Block body() {
      return this.body;
   }

   public Synchronized body(Block body) {
      Synchronized_c n = (Synchronized_c)this.copy();
      n.body = body;
      return n;
   }

   protected Synchronized_c reconstruct(Expr expr, Block body) {
      if (expr == this.expr && body == this.body) {
         return this;
      } else {
         Synchronized_c n = (Synchronized_c)this.copy();
         n.expr = expr;
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      Block body = (Block)this.visitChild(this.body, v);
      return this.reconstruct(expr, body);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.isSubtype(this.expr.type(), ts.Object())) {
         throw new SemanticException("Cannot synchronize on an expression of type \"" + this.expr.type() + "\".", this.expr.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Object() : child.type());
   }

   public String toString() {
      return "synchronized (" + this.expr + ") { ... }";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("synchronized (");
      this.printBlock(this.expr, w, tr);
      w.write(") ");
      this.printSubStmt(this.body, w, tr);
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.expr, (Term)this.body.entry());
      v.visitCFG(this.body, (Term)this);
      return succs;
   }
}
