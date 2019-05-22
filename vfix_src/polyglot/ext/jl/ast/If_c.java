package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.If;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class If_c extends Stmt_c implements If {
   protected Expr cond;
   protected Stmt consequent;
   protected Stmt alternative;

   public If_c(Position pos, Expr cond, Stmt consequent, Stmt alternative) {
      super(pos);
      this.cond = cond;
      this.consequent = consequent;
      this.alternative = alternative;
   }

   public Expr cond() {
      return this.cond;
   }

   public If cond(Expr cond) {
      If_c n = (If_c)this.copy();
      n.cond = cond;
      return n;
   }

   public Stmt consequent() {
      return this.consequent;
   }

   public If consequent(Stmt consequent) {
      If_c n = (If_c)this.copy();
      n.consequent = consequent;
      return n;
   }

   public Stmt alternative() {
      return this.alternative;
   }

   public If alternative(Stmt alternative) {
      If_c n = (If_c)this.copy();
      n.alternative = alternative;
      return n;
   }

   protected If_c reconstruct(Expr cond, Stmt consequent, Stmt alternative) {
      if (cond == this.cond && consequent == this.consequent && alternative == this.alternative) {
         return this;
      } else {
         If_c n = (If_c)this.copy();
         n.cond = cond;
         n.consequent = consequent;
         n.alternative = alternative;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr cond = (Expr)this.visitChild(this.cond, v);
      Stmt consequent = (Stmt)this.visitChild(this.consequent, v);
      Stmt alternative = (Stmt)this.visitChild(this.alternative, v);
      return this.reconstruct(cond, consequent, alternative);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.equals(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("Condition of if statement must have boolean type.", this.cond.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.cond ? ts.Boolean() : child.type());
   }

   public String toString() {
      return "if (" + this.cond + ") " + this.consequent + (this.alternative != null ? " else " + this.alternative : "");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("if (");
      this.printBlock(this.cond, w, tr);
      w.write(")");
      this.printSubStmt(this.consequent, w, tr);
      if (this.alternative != null) {
         if (this.consequent instanceof Block) {
            w.write(" ");
         } else {
            w.allowBreak(0, " ");
         }

         w.write("else");
         this.printSubStmt(this.alternative, w, tr);
      }

   }

   public Term entry() {
      return this.cond.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.alternative == null) {
         v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this);
         v.visitCFG(this.consequent, (Term)this);
      } else {
         v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this.alternative.entry());
         v.visitCFG(this.consequent, (Term)this);
         v.visitCFG(this.alternative, (Term)this);
      }

      return succs;
   }
}
